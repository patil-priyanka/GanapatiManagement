package com.shreeganesh.ganapatimanagement.domain.usecase.home

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.AnnouncementItem
import kotlinx.coroutines.flow.Flow

class GetAnnouncementUseCase(private val repository: GanapatiRepository) {
    operator fun invoke(): Flow<AnnouncementItem?> = repository.latestAnnouncement
}
