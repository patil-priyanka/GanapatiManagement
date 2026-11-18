package com.shreeganesh.ganapatimanagement.domain.usecase.home

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.AartiEvent
import kotlinx.coroutines.flow.Flow

class GetTodayScheduleUseCase(private val repository: GanapatiRepository) {
    operator fun invoke(): Flow<List<AartiEvent>> = repository.todaySchedule
}
