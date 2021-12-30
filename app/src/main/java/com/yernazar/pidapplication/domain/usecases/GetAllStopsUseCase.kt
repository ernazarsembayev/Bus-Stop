package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.domain.repository.AppRepository
import com.yernazar.pidapplication.data.repository.model.Stop

class GetAllStopsUseCase(private val appRepository: AppRepository) {

    suspend fun execute(): List<Stop> {

        return appRepository.getAllStops()

    }

}