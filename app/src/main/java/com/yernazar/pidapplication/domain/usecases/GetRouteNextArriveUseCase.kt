package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.domain.repository.AppRepository
import com.yernazar.pidapplication.data.repository.model.Route

class GetRouteNextArriveUseCase(private val appRepository: AppRepository) {

    suspend fun execute(stopUid: String): List<Route> {

        return appRepository.getRouteNextArrive(stopUid)

    }

}