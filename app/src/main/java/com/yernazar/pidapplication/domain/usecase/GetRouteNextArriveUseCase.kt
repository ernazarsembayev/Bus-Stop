package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.data.repository.model.RouteAndNextArrive
import com.yernazar.pidapplication.domain.repository.AppRepository

class GetRouteNextArriveUseCase(private val appRepository: AppRepository) {

    suspend fun execute(stopUid: String): List<RouteAndNextArrive> {

        return appRepository.getRouteNextArrive(stopUid)

    }

}