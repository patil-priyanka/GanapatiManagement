package com.shreeganesh.ganapatimanagement.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shreeganesh.ganapatimanagement.data.local.AppDatabase
import com.shreeganesh.ganapatimanagement.data.repository.GanapatiRepositoryImpl
import com.shreeganesh.ganapatimanagement.domain.usecase.home.GetAnnouncementUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.home.GetFestivalSummaryUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.home.GetRecentActivitiesUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.home.GetTodayScheduleUseCase
import com.shreeganesh.ganapatimanagement.model.AartiEvent
import com.shreeganesh.ganapatimanagement.model.AnnouncementItem
import com.shreeganesh.ganapatimanagement.model.FestivalSummary
import com.shreeganesh.ganapatimanagement.model.RecentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/** UI state exposed by [HomeViewModel]. */
data class HomeUiState(
    val festivalSummary: FestivalSummary = FestivalSummary(),
    val announcement: AnnouncementItem? = null,
    val recentActivities: List<RecentActivity> = emptyList(),
    val todaySchedule: List<AartiEvent> = emptyList(),
    /** True only on the very first load before any data arrives. */
    val isLoading: Boolean = true,
    /** Non-null when a data-layer error has occurred. */
    val errorMessage: String? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GanapatiRepositoryImpl(AppDatabase.getDatabase(application))

    private val getFestivalSummary = GetFestivalSummaryUseCase(repository)
    private val getRecentActivities = GetRecentActivitiesUseCase(repository)
    private val getTodaySchedule = GetTodayScheduleUseCase(repository)
    private val getAnnouncement = GetAnnouncementUseCase(repository)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        // Festival summary
        getFestivalSummary()
            .onEach { summary ->
                _uiState.update { it.copy(festivalSummary = summary, isLoading = false) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
            .launchIn(viewModelScope)

        // Announcement
        getAnnouncement()
            .onEach { item ->
                _uiState.update { it.copy(announcement = item) }
            }
            .catch { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
            .launchIn(viewModelScope)

        // Recent activities
        getRecentActivities()
            .onEach { activities ->
                _uiState.update { it.copy(recentActivities = activities) }
            }
            .catch { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
            .launchIn(viewModelScope)

        // Today's schedule
        getTodaySchedule()
            .onEach { schedule ->
                _uiState.update { it.copy(todaySchedule = schedule) }
            }
            .catch { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
            .launchIn(viewModelScope)
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
