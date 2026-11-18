package com.shreeganesh.ganapatimanagement.domain.usecase.home

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.FestivalSummary
import kotlinx.coroutines.flow.Flow

class GetFestivalSummaryUseCase(private val repository: GanapatiRepository) {
    operator fun invoke(): Flow<FestivalSummary> = repository.festivalSummary
}
