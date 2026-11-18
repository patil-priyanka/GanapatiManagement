package com.shreeganesh.ganapatimanagement.domain.usecase.member

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository

class DeleteMemberUseCase(private val repository: GanapatiRepository) {
    suspend operator fun invoke(memberId: String) = repository.deleteMember(memberId)
}
