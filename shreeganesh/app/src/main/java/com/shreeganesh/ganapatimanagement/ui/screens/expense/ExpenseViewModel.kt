package com.shreeganesh.ganapatimanagement.ui.screens.expense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shreeganesh.ganapatimanagement.data.local.AppDatabase
import com.shreeganesh.ganapatimanagement.data.repository.GanapatiRepositoryImpl
import com.shreeganesh.ganapatimanagement.domain.usecase.expense.AddExpenseUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.expense.DeleteExpenseUseCase
import com.shreeganesh.ganapatimanagement.domain.usecase.expense.GetExpensesUseCase
import com.shreeganesh.ganapatimanagement.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** UI state exposed by [ExpenseViewModel]. */
data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GanapatiRepositoryImpl(AppDatabase.getDatabase(application))

    private val getExpenses = GetExpensesUseCase(repository)
    private val addExpenseUseCase = AddExpenseUseCase(repository)
    private val deleteExpenseUseCase = DeleteExpenseUseCase(repository)

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    init {
        observeExpenses()
    }

    private fun observeExpenses() {
        getExpenses()
            .onEach { list ->
                _uiState.update { it.copy(expenses = list, isLoading = false, errorMessage = null) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "अज्ञात त्रुटी") }
            }
            .launchIn(viewModelScope)
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                addExpenseUseCase(expense)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "खर्च जोडणे अयशस्वी") }
            }
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            try {
                deleteExpenseUseCase(expenseId)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "खर्च हटवणे अयशस्वी") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
                return ExpenseViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
