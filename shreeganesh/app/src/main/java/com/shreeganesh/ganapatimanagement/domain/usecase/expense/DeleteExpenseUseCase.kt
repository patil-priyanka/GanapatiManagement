package com.shreeganesh.ganapatimanagement.domain.usecase.expense

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository

class DeleteExpenseUseCase(private val repository: GanapatiRepository) {
    suspend operator fun invoke(expenseId: String) = repository.deleteExpense(expenseId)
}
