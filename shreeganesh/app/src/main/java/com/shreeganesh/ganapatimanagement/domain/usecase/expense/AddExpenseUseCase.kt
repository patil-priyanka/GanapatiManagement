package com.shreeganesh.ganapatimanagement.domain.usecase.expense

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.Expense

class AddExpenseUseCase(private val repository: GanapatiRepository) {
    suspend operator fun invoke(expense: Expense) = repository.addExpense(expense)
}
