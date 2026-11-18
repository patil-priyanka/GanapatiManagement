package com.shreeganesh.ganapatimanagement.domain.usecase.karyakram

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.KaryakramItem
import kotlinx.coroutines.flow.Flow

class GetKaryakramUseCase(private val repository: GanapatiRepository) {
    operator fun invoke(): Flow<List<KaryakramItem>> = repository.allKaryakram
}
