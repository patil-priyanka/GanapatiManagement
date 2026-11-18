package com.shreeganesh.ganapatimanagement.ui.screens.karyakram

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shreeganesh.ganapatimanagement.data.local.AppDatabase
import com.shreeganesh.ganapatimanagement.data.repository.GanapatiRepositoryImpl
import com.shreeganesh.ganapatimanagement.domain.usecase.karyakram.AddKaryakramUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.karyakram.DeleteKaryakramUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.karyakram.GetKaryakramUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.karyakram.UpdateKaryakramUseCase
import com.shreeganesh.ganapatimanagement.model.EventStatus
import com.shreeganesh.ganapatimanagement.model.KaryakramItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** UI state exposed by [KaryakramViewModel]. */
data class KaryakramUiState(
    val karyakramList: List<KaryakramItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class KaryakramViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GanapatiRepositoryImpl(AppDatabase.getDatabase(application))

    private val getKaryakram = GetKaryakramUseCase(repository)
    private val addKaryakramUseCase = AddKaryakramUseCase(repository)
    private val updateKaryakramUseCase = UpdateKaryakramUseCase(repository)
    private val deleteKaryakramUseCase = DeleteKaryakramUseCase(repository)

    private val _uiState = MutableStateFlow(KaryakramUiState())
    val uiState: StateFlow<KaryakramUiState> = _uiState.asStateFlow()

    init {
        observeKaryakram()
    }

    private fun observeKaryakram() {
        getKaryakram()
            .onEach { list ->
                _uiState.update { it.copy(karyakramList = list, isLoading = false, errorMessage = null) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "अज्ञात त्रुटी") }
            }
            .launchIn(viewModelScope)
    }

    fun addKaryakram(item: KaryakramItem) {
        viewModelScope.launch {
            try {
                addKaryakramUseCase(item)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "कार्यक्रम जोडणे अयशस्वी") }
            }
        }
    }

    fun updateKaryakram(item: KaryakramItem) {
        viewModelScope.launch {
            try {
                updateKaryakramUseCase(item)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "कार्यक्रम अपडेट अयशस्वी") }
            }
        }
    }

    fun updateKaryakramStatus(id: String, status: EventStatus) {
        viewModelScope.launch {
            try {
                updateKaryakramUseCase.updateStatus(id, status)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "स्थिती अपडेट अयशस्वी") }
            }
        }
    }

    fun deleteKaryakram(id: String) {
        viewModelScope.launch {
            try {
                deleteKaryakramUseCase(id)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "कार्यक्रम हटवणे अयशस्वी") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(KaryakramViewModel::class.java)) {
                return KaryakramViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
