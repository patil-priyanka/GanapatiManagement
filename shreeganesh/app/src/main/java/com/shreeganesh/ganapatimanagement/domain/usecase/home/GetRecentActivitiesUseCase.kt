package com.shreeganesh.ganapatimanagement.domain.usecase.home

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.RecentActivity
import kotlinx.coroutines.flow.Flow

class GetRecentActivitiesUseCase(private val repository: GanapatiRepository) {
    operator fun invoke(): Flow<List<RecentActivity>> = repository.recentActivities
}
