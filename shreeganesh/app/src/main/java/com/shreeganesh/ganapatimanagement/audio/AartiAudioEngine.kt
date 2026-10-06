package com.shreeganesh.ganapatimanagement.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

class AartiAudioEngine(
    private val scope: CoroutineScope
) {
    private val sampleRate = 22050

    // Playback state
    var isPlaying: Boolean = false
        private set

    var currentPositionSeconds: Int = 0
        private set

    var totalDurationSeconds: Int = 225
        private set

    var isBellLoopActive: Boolean = false
        private set

    private var playbackJob: Job? = null
    private var bellLoopJob: Job? = null

    var onProgressUpdate: ((currentSec: Int, totalSec: Int) -> Unit)? = null
    var onPlaybackStateChanged: ((Boolean) -> Unit)? = null
    var onAartiCompleted: (() -> Unit)? = null

    /**
     * Start or resume playing the Aarti rhythm & devotional beats
     */
    fun play(durationSeconds: Int = totalDurationSeconds) {
        if (isPlaying) return
        totalDurationSeconds = durationSeconds
        isPlaying = true
        onPlaybackStateChanged?.invoke(true)

        playbackJob = scope.launch(Dispatchers.Default) {
            val beatIntervalMs = 750L // ~80 BPM (standard Aarti rhythm)
            var beatCounter = 0

            while (isActive && isPlaying) {
                // Play rhythmic taal / chime beat
                if (beatCounter % 4 == 0) {
                    playTone(frequency = 880.0, durationMs = 280, volume = 0.55f)
                } else {
                    playTone(frequency = 1320.0, durationMs = 120, volume = 0.35f)
                }

                delay(beatIntervalMs)
                beatCounter++

                // Update second counter approximately
                if (beatCounter % 2 == 0) {
                    currentPositionSeconds++
                    onProgressUpdate?.invoke(currentPositionSeconds, totalDurationSeconds)

                    if (currentPositionSeconds >= totalDurationSeconds) {
                        currentPositionSeconds = 0
                        pause()
                        onAartiCompleted?.invoke()
                        break
                    }
                }
            }
        }
    }

    /**
     * Pause Aarti playback
     */
    fun pause() {
        isPlaying = false
        playbackJob?.cancel()
        playbackJob = null
        onPlaybackStateChanged?.invoke(false)
    }

    /**
     * Toggle play/pause
     */
    fun togglePlayPause(durationSeconds: Int = totalDurationSeconds) {
        if (isPlaying) {
            pause()
        } else {
            play(durationSeconds)
        }
    }

    /**
     * Seek to a specific second
     */
    fun seekTo(seconds: Int) {
        currentPositionSeconds = seconds.coerceIn(0, totalDurationSeconds)
        onProgressUpdate?.invoke(currentPositionSeconds, totalDurationSeconds)
    }

    /**
     * Ring a single authentic bronze temple bell (घंटी)
     */
    fun ringTempleBell() {
        scope.launch(Dispatchers.Default) {
            playTempleBellSound()
        }
    }

    /**
     * Toggle continuous bell ringing in background during aarti
     */
    fun toggleBellLoop() {
        isBellLoopActive = !isBellLoopActive
        if (isBellLoopActive) {
            bellLoopJob = scope.launch(Dispatchers.Default) {
                while (isActive && isBellLoopActive) {
                    playTempleBellSound(volume = 0.45f)
                    delay(1200L)
                }
            }
        } else {
            bellLoopJob?.cancel()
            bellLoopJob = null
        }
    }

    /**
     * Blow the sacred Shankh (शंख नाद)
     */
    fun blowShankh() {
        scope.launch(Dispatchers.Default) {
            playShankhSound()
        }
    }

    /**
     * Synthesize and output a temple bell tone using AudioTrack
     */
    private fun playTempleBellSound(volume: Float = 0.8f) {
        val durationMs = 1800
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        val fundamental = 987.77 // B5 note - classic temple bell pitch

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val envelope = exp(-3.2 * t) // Natural acoustic decay

            // Overtones of a bell (inharmonic series)
            val wave = 0.55 * sin(2.0 * PI * fundamental * t) +
                    0.25 * sin(2.0 * PI * (fundamental * 1.50) * t) +
                    0.15 * sin(2.0 * PI * (fundamental * 2.76) * t) +
                    0.05 * sin(2.0 * PI * (fundamental * 4.07) * t)

            val sample = (wave * envelope * volume * Short.MAX_VALUE).toInt()
            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        writeToAudioTrack(buffer)
    }

    /**
     * Synthesize and output sacred Shankh drone
     */
    private fun playShankhSound() {
        val durationMs = 2800
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        val fundamental = 220.0 // A3 note - deep sacred drone

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate

            // Attack, sustain, and release envelope
            val envelope = when {
                t < 0.4 -> (t / 0.4) // gentle breath attack
                t > 2.2 -> ((2.8 - t) / 0.6).coerceAtLeast(0.0) // smooth release
                else -> 1.0 // sustained drone
            }

            // Natural slight vibrato
            val vibrato = 1.0 + 0.015 * sin(2.0 * PI * 5.0 * t)
            val freq = fundamental * vibrato

            // Rich warm brass/conch harmonics
            val wave = 0.50 * sin(2.0 * PI * freq * t) +
                    0.30 * sin(2.0 * PI * (freq * 2.0) * t) +
                    0.15 * sin(2.0 * PI * (freq * 3.0) * t) +
                    0.05 * sin(2.0 * PI * (freq * 4.0) * t)

            val sample = (wave * envelope * 0.75f * Short.MAX_VALUE).toInt()
            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        writeToAudioTrack(buffer)
    }

    /**
     * Synthesize and output rhythmic pulse / chime
     */
    private fun playTone(frequency: Double, durationMs: Int, volume: Float) {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val envelope = exp(-12.0 * t) // Fast percussive decay
            val wave = sin(2.0 * PI * frequency * t) + 0.3 * sin(2.0 * PI * (frequency * 1.5) * t)
            val sample = (wave * envelope * volume * Short.MAX_VALUE).toInt()
            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        writeToAudioTrack(buffer)
    }

    private fun writeToAudioTrack(buffer: ShortArray) {
        try {
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(buffer.size * 2)

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()

            // Release track after it finishes
            scope.launch(Dispatchers.Default) {
                val durationMs = ((buffer.size.toDouble() / sampleRate) * 1000).toLong() + 100L
                delay(durationMs)
                try {
                    track.stop()
                    track.release()
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {
            // Graceful fallback
        }
    }

    fun release() {
        isPlaying = false
        isBellLoopActive = false
        playbackJob?.cancel()
        bellLoopJob?.cancel()
    }
}
