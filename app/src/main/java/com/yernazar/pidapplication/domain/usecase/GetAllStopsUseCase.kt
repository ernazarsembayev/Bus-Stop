package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.domain.repository.AppRepository
import org.jguniverse.pidapplicationgm.repo.model.Stop

class GetAllStopsUseCase(private val appRepository: AppRepository) {

    suspend fun execute(): List<Stop> {

        return appRepository.getAllStops()

    }

}