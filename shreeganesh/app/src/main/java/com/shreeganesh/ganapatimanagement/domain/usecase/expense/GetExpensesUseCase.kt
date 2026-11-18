package com.shreeganesh.ganapatimanagement.domain.usecase.expense

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.Expense
import kotlinx.coroutines.flow.Flow

class GetExpensesUseCase(private val repository: GanapatiRepository) {
    operator fun invoke(): Flow<List<Expense>> = repository.allExpenses
}
