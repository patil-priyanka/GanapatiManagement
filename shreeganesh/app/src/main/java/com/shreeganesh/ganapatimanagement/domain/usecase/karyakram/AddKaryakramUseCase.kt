package com.shreeganesh.ganapatimanagement.domain.usecase.karyakram

import com.shreeganesh.ganapatimanagement.domain.repository.GanapatiRepository
import com.shreeganesh.ganapatimanagement.model.KaryakramItem

class AddKaryakramUseCase(private val repository: GanapatiRepository) {
    suspend operator fun invoke(item: KaryakramItem) = repository.addKaryakram(item)
}
