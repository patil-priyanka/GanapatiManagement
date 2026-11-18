package com.shreeganesh.ganapatimanagement.domain.usecase.donation

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository

class DeleteDonationUseCase(private val repository: GanapatiRepository) {
    suspend operator fun invoke(donationId: String) = repository.deleteDonation(donationId)
}
