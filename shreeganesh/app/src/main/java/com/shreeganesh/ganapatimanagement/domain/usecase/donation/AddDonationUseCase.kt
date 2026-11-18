package com.shreeganesh.ganapatimanagement.domain.usecase.donation

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.Donation

class AddDonationUseCase(private val repository: GanapatiRepository) {
    suspend operator fun invoke(donation: Donation) = repository.addDonation(donation)
}
