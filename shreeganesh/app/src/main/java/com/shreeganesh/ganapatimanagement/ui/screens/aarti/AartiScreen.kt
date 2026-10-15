package com.shreeganesh.ganapatimanagement.ui.screens.aarti

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreeganesh.ganapatimanagement.audio.AartiAudioEngine
import com.shreeganesh.ganapatimanagement.model.AartiItem
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBackgroundLight
import com.shreeganesh.ganapatimanagement.ui.theme.FestiveBorderLight
import com.shreeganesh.ganapatimanagement.ui.theme.GanapatiManagementTheme
import com.shreeganesh.ganapatimanagement.ui.theme.MarigoldGold
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronDeep
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronLight
import com.shreeganesh.ganapatimanagement.ui.theme.SaffronPrimary
import com.shreeganesh.ganapatimanagement.ui.theme.TextPrimaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.TextSecondaryLight
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionDeep
import com.shreeganesh.ganapatimanagement.ui.theme.VermilionRed
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AartiScreen(
    modifier: Modifier = Modifier,
    aartiList: List<AartiItem> = emptyList(),
    initialAartiId: String? = null,
    onBackClick: () -> Unit = {}
) {
    if (aartiList.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize().background(FestiveBackgroundLight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "आरती संग्रह लोड होत आहे...",
                color = SaffronDeep,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
        return
    }

    val playlist = aartiList
    val initialIndex = remember(initialAartiId, playlist) {
        if (initialAartiId != null) {
            val found = playlist.indexOfFirst { it.id == initialAartiId }
            if (found >= 0) found else 0
        } else 0
    }

    var selectedIndex by remember(playlist) { mutableIntStateOf(initialIndex.coerceIn(0, playlist.size - 1)) }
    val currentAarti = playlist[selectedIndex.coerceIn(0, playlist.size - 1)]

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current

    // Audio Engine
    val audioEngine = remember { AartiAudioEngine(scope) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentSeconds by remember { mutableIntStateOf(0) }
    var totalSeconds by remember { mutableIntStateOf(currentAarti.durationSeconds) }
    var isBellContinuous by remember { mutableStateOf(false) }

    // Lyrics font size state
    var fontSizeMultiplier by remember { mutableFloatStateOf(1.0f) }

    fun showMessage(msg: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Connect Audio Engine callbacks
    DisposableEffect(audioEngine) {
        audioEngine.onPlaybackStateChanged = { playing ->
            isPlaying = playing
        }
        audioEngine.onProgressUpdate = { current, total ->
            currentSeconds = current
            totalSeconds = total
        }
        audioEngine.onAartiCompleted = {
            // Auto advance to next Aarti
            if (selectedIndex < playlist.size - 1) {
                selectedIndex++
                audioEngine.play(playlist[selectedIndex].durationSeconds)
            } else {
                selectedIndex = 0
                currentSeconds = 0
            }
        }
        onDispose {
            audioEngine.release()
        }
    }

    // When a user selects a different Aarti from the playlist
    fun selectAarti(newIndex: Int, autoPlay: Boolean = false) {
        if (newIndex in playlist.indices) {
            selectedIndex = newIndex
            currentSeconds = 0
            totalSeconds = playlist[newIndex].durationSeconds
            if (autoPlay || isPlaying) {
                audioEngine.pause()
                audioEngine.play(playlist[newIndex].durationSeconds)
            }
        }
    }

    // Animation for Diya rotation when audio is playing
    val infiniteTransition = rememberInfiniteTransition(label = "diyaRotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FestiveBackgroundLight,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "आरती संग्रह",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "ऑफलाइन ऑडिओ व संपूर्ण शब्द",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        audioEngine.pause()
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "मागे जा",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Font Size Toggle
                    IconButton(onClick = {
                        fontSizeMultiplier = if (fontSizeMultiplier >= 1.3f) 0.9f else fontSizeMultiplier + 0.15f
                        showMessage("अक्षर आकार बदलला")
                    }) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "अक्षर आकार",
                            tint = Color.White
                        )
                    }

                    // Share lyrics
                    IconButton(onClick = {
                        showMessage("आरती शब्द कॉपी व शेअर करा")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "शेअर करा",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaffronDeep
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Audio Player Hero Card with Animated Aarti Thali
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.verticalGradient(
                            listOf(FestiveBorderLight, SaffronLight.copy(alpha = 0.4f))
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Deity Badge
                        Surface(
                            color = SaffronDeep.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "॥ ${currentAarti.deity} ॥ • ${currentAarti.author}",
                                color = SaffronDeep,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Large Aarti Title
                        Text(
                            text = currentAarti.marathiTitle,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimaryLight,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = currentAarti.description,
                            fontSize = 12.sp,
                            color = TextSecondaryLight,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Animated Aarti Diya Thali Centerpiece
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(110.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        listOf(
                                            MarigoldGold.copy(alpha = 0.25f),
                                            SaffronLight.copy(alpha = 0.15f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        ) {
                            // Rotating Aarti Diya Motif
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .rotate(if (isPlaying) rotationAngle else 0f)
                                    .background(Color(0xFFFFF3E0), CircleShape)
                                    .border(2.dp, MarigoldGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🪔",
                                    fontSize = 42.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Progress Slider
                        val progress = if (totalSeconds > 0) currentSeconds.toFloat() / totalSeconds else 0f
                        Slider(
                            value = progress,
                            onValueChange = { newProgress ->
                                val targetSec = (newProgress * totalSeconds).toInt()
                                currentSeconds = targetSec
                                audioEngine.seekTo(targetSec)
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = SaffronPrimary,
                                activeTrackColor = SaffronPrimary,
                                inactiveTrackColor = Color(0xFFF3ECE4)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Time Labels
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = formatTime(currentSeconds),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaffronDeep
                            )
                            Text(
                                text = "ऑफलाइन ऑडिओ ताल",
                                fontSize = 10.sp,
                                color = TextSecondaryLight
                            )
                            Text(
                                text = formatTime(totalSeconds),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondaryLight
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Main Audio Controls Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Previous Aarti
                            IconButton(
                                onClick = {
                                    if (selectedIndex > 0) {
                                        selectAarti(selectedIndex - 1, autoPlay = isPlaying)
                                    } else {
                                        selectAarti(playlist.size - 1, autoPlay = isPlaying)
                                    }
                                },
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(Color(0xFFFFF7ED), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "मागील आरती",
                                    tint = SaffronDeep,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            // Big Play / Pause Button
                            Surface(
                                shape = CircleShape,
                                color = SaffronPrimary,
                                shadowElevation = 6.dp,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clickable {
                                        audioEngine.togglePlayPause(currentAarti.durationSeconds)
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlaying) "थांबवा" else "चालू करा",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            // Next Aarti
                            IconButton(
                                onClick = {
                                    if (selectedIndex < playlist.size - 1) {
                                        selectAarti(selectedIndex + 1, autoPlay = isPlaying)
                                    } else {
                                        selectAarti(0, autoPlay = isPlaying)
                                    }
                                },
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(Color(0xFFFFF7ED), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "पुढील आरती",
                                    tint = SaffronDeep,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Interactive Devotional Instruments Toolbar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Temple Bell (घंटा नाद)
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        audioEngine.ringTempleBell()
                                        showMessage("🔔 घंटा नाद वाजवला!")
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFFF8E1),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MarigoldGold.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(text = "🔔", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "घंटा नाद",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SaffronDeep
                                    )
                                }
                            }

                            // Continuous Bell Toggle
                            Surface(
                                modifier = Modifier
                                    .weight(1.1f)
                                    .clickable {
                                        audioEngine.toggleBellLoop()
                                        isBellContinuous = audioEngine.isBellLoopActive
                                        if (isBellContinuous) {
                                            showMessage("सतत घंटी वादन सुरू केले")
                                        } else {
                                            showMessage("सतत घंटी वादन बंद केले")
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isBellContinuous) SaffronPrimary else Color(0xFFFFF3E0),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SaffronDeep.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(text = "🪔", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isBellContinuous) "घंटी सुरू" else "सतत घंटी",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isBellContinuous) Color.White else SaffronDeep
                                    )
                                }
                            }

                            // Shankh Naad (शंख नाद)
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        audioEngine.blowShankh()
                                        showMessage("🐚 पवित्र शंख नाद!")
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF3ECE4),
                                border = androidx.compose.foundation.BorderStroke(1.dp, FestiveBorderLight)
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(text = "🐚", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "शंख नाद",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryLight
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Horizontal Aarti Selector Carousel
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "आरती यादी (सर्व ६ आरत्या)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(playlist) { index, aarti ->
                            val isSelected = index == selectedIndex
                            Surface(
                                modifier = Modifier.clickable {
                                    selectAarti(index, autoPlay = isPlaying)
                                },
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) SaffronPrimary else Color.White,
                                shadowElevation = if (isSelected) 3.dp else 1.dp,
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, FestiveBorderLight)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = when (aarti.deity) {
                                            "श्री गणेश" -> "🌺"
                                            "श्री शंकर" -> "🔱"
                                            "श्री दुर्गा देवी" -> "🚩"
                                            else -> "🌸"
                                        },
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = aarti.marathiTitle,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else TextPrimaryLight
                                        )
                                        Text(
                                            text = formatTime(aarti.durationSeconds),
                                            fontSize = 10.sp,
                                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else TextSecondaryLight
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. Lyrics Card (संपूर्ण शब्द)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(FestiveBorderLight))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Lyrics Header & Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "📖 संपूर्ण आरती शब्द",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryLight
                                )
                            }

                            // Copy Button
                            Surface(
                                modifier = Modifier.clickable {
                                    clipboardManager.setText(AnnotatedString(currentAarti.lyrics))
                                    showMessage("आरतीचे शब्द कॉपी केले!")
                                },
                                shape = RoundedCornerShape(8.dp),
                                color = SaffronPrimary.copy(alpha = 0.1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "कॉपी करा",
                                        tint = SaffronDeep,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "कॉपी",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SaffronDeep
                                    )
                                }
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = FestiveBorderLight,
                            thickness = 1.dp
                        )

                        // Full Marathi Lyrics
                        Text(
                            text = currentAarti.lyrics,
                            fontSize = (15 * fontSizeMultiplier).sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimaryLight,
                            lineHeight = (26 * fontSizeMultiplier).sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Auspicious Footer
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "॥ ॐ गं गणपतये नमः ॥",
                        color = SaffronDeep,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "गणपती बाप्पा मोरया • मंगलमूर्ती मोरया",
                        color = TextSecondaryLight,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format(java.util.Locale.ENGLISH, "%02d:%02d", m, s)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AartiScreenPreview() {
    GanapatiManagementTheme {
        AartiScreen()
    }
}
