package com.shreeganesh.ganapatimanagement.domain.usecase.member

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.Member

class AddMemberUseCase(private val repository: GanapatiRepository) {
    suspend operator fun invoke(member: Member) = repository.addMember(member)
}
