package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.domain.repository.AppRepository
import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.data.repository.server.response.routeTimeResponse.RouteTime

class GetRouteNextArriveUseCase(private val appRepository: AppRepository) {

    suspend fun execute(stopUid: String): List<RouteTime> {

        return appRepository.getRouteNextArrive(stopUid)

    }

}