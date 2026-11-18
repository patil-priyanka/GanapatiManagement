package com.shreeganesh.ganapatimanagement.domain.usecase.donation

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.Donation
import kotlinx.coroutines.flow.Flow

class GetDonationsUseCase(private val repository: GanapatiRepository) {
    operator fun invoke(): Flow<List<Donation>> = repository.allDonations
}
