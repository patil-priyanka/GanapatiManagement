package com.shreeganesh.ganapatimanagement.core.util

/**
 * A generic wrapper that represents the state of a data operation.
 * Used to communicate Loading / Success / Error states from the data
 * layer up through the domain layer to the UI.
 */
sealed class Resource<out T> {
    /** Data is being loaded (optionally carries stale cached data) */
    data class Loading<T>(val cachedData: T? = null) : Resource<T>()

    /** Operation succeeded with [data] */
    data class Success<T>(val data: T) : Resource<T>()

    /** Operation failed with an [message] and optional [throwable] */
    data class Error<T>(
        val message: String,
        val throwable: Throwable? = null,
        val cachedData: T? = null
    ) : Resource<T>()
}

/** Convenience – returns the data or null regardless of state */
fun <T> Resource<T>.dataOrNull(): T? = when (this) {
    is Resource.Success -> data
    is Resource.Loading -> cachedData
    is Resource.Error -> cachedData
}

/** Returns true only when this is a [Resource.Loading] */
val <T> Resource<T>.isLoading: Boolean get() = this is Resource.Loading

/** Returns true only when this is a [Resource.Error] */
val <T> Resource<T>.isError: Boolean get() = this is Resource.Error
