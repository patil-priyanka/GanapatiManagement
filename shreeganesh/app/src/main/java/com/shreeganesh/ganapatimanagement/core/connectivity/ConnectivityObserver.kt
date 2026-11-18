package com.shreeganesh.ganapatimanagement.core.connectivity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for observing network connectivity.
 * Implementations should emit the current connectivity state and
 * update whenever the connection changes.
 */
interface ConnectivityObserver {
    /** Emits `true` when the device has an active internet connection. */
    val isConnected: Flow<Boolean>
}
