package com.shreeganesh.ganapatimanagement.ui.screens.member

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shreeganesh.ganapatimanagement.data.local.AppDatabase
import com.shreeganesh.ganapatimanagement.data.repository.GanapatiRepositoryImpl
import com.shreeganesh.ganapatimanagement.domain.usecase.member.AddMemberUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.member.DeleteMemberUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.member.GetMembersUseCase
import com.shreeganesh.ganapatimanagement.model.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** UI state exposed by [MemberViewModel]. */
data class MemberUiState(
    val members: List<Member> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class MemberViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GanapatiRepositoryImpl(AppDatabase.getDatabase(application))

    private val getMembers = GetMembersUseCase(repository)
    private val addMemberUseCase = AddMemberUseCase(repository)
    private val deleteMemberUseCase = DeleteMemberUseCase(repository)

    private val _uiState = MutableStateFlow(MemberUiState())
    val uiState: StateFlow<MemberUiState> = _uiState.asStateFlow()

    init {
        observeMembers()
    }

    private fun observeMembers() {
        getMembers()
            .onEach { list ->
                _uiState.update { it.copy(members = list, isLoading = false, errorMessage = null) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "अज्ञात त्रुटी") }
            }
            .launchIn(viewModelScope)
    }

    fun addMember(member: Member) {
        viewModelScope.launch {
            try {
                addMemberUseCase(member)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "सदस्य जोडणे अयशस्वी") }
            }
        }
    }

    fun deleteMember(memberId: String) {
        viewModelScope.launch {
            try {
                deleteMemberUseCase(memberId)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "सदस्य हटवणे अयशस्वी") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MemberViewModel::class.java)) {
                return MemberViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
