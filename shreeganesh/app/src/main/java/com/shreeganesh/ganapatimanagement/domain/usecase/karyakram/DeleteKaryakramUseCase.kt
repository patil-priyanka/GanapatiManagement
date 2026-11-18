package com.shreeganesh.ganapatimanagement.domain.usecase.karyakram

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository

class DeleteKaryakramUseCase(private val repository: GanapatiRepository) {
    suspend operator fun invoke(id: String) = repository.deleteKaryakram(id)
}
