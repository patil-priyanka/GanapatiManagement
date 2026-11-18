package com.shreeganesh.ganapatimanagement.ui.screens.aarti

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shreeganesh.ganapatimanagement.data.local.AppDatabase
import com.shreeganesh.ganapatimanagement.data.repository.GanapatiRepositoryImpl
import com.shreeganesh.ganapatimanagement.model.AartiItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/** UI state exposed by [AartiViewModel]. */
data class AartiUiState(
    val aartis: List<AartiItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class AartiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GanapatiRepositoryImpl(AppDatabase.getDatabase(application))

    private val _uiState = MutableStateFlow(AartiUiState())
    val uiState: StateFlow<AartiUiState> = _uiState.asStateFlow()

    init {
        observeAartis()
    }

    private fun observeAartis() {
        repository.allAartis
            .onEach { list ->
                _uiState.update { it.copy(aartis = list, isLoading = false, errorMessage = null) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "अज्ञात त्रुटी") }
            }
            .launchIn(viewModelScope)
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AartiViewModel::class.java)) {
                return AartiViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
