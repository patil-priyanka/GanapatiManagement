package com.shreeganesh.ganapatimanagement.domain.usecase.member

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.Member
import kotlinx.coroutines.flow.Flow

class GetMembersUseCase(private val repository: GanapatiRepository) {
    operator fun invoke(): Flow<List<Member>> = repository.allMembers
}
