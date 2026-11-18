package com.shreeganesh.ganapatimanagement.domain.usecase.karyakram

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.EventStatus
import com.shreeganesh.ganapatimanagement.model.KaryakramItem

class UpdateKaryakramUseCase(private val repository: GanapatiRepository) {
    suspend operator fun invoke(item: KaryakramItem) = repository.updateKaryakram(item)
    suspend fun updateStatus(id: String, status: EventStatus) =
        repository.updateKaryakramStatus(id, status)
}
