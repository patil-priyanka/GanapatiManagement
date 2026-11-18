package com.shreeganesh.ganapatimanagement.ui.screens.donation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shreeganesh.ganapatimanagement.data.local.AppDatabase
import com.shreeganesh.ganapatimanagement.data.repository.GanapatiRepositoryImpl
import com.shreeganesh.ganapatimanagement.domain.usecase.donation.AddDonationUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.donation.DeleteDonationUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.donation.GetDonationsUseCase
import com.shreeganesh.ganapatimanagement.model.Donation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** UI state exposed by [DonationViewModel]. */
data class DonationUiState(
    val donations: List<Donation> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class DonationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GanapatiRepositoryImpl(AppDatabase.getDatabase(application))

    private val getDonations = GetDonationsUseCase(repository)
    private val addDonationUseCase = AddDonationUseCase(repository)
    private val deleteDonationUseCase = DeleteDonationUseCase(repository)

    private val _uiState = MutableStateFlow(DonationUiState())
    val uiState: StateFlow<DonationUiState> = _uiState.asStateFlow()

    init {
        observeDonations()
    }

    private fun observeDonations() {
        getDonations()
            .onEach { list ->
                _uiState.update { it.copy(donations = list, isLoading = false, errorMessage = null) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "अज्ञात त्रुटी") }
            }
            .launchIn(viewModelScope)
    }

    fun addDonation(donation: Donation) {
        viewModelScope.launch {
            try {
                addDonationUseCase(donation)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "देणगी जोडणे अयशस्वी") }
            }
        }
    }

    fun deleteDonation(donationId: String) {
        viewModelScope.launch {
            try {
                deleteDonationUseCase(donationId)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "देणगी हटवणे अयशस्वी") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DonationViewModel::class.java)) {
                return DonationViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
