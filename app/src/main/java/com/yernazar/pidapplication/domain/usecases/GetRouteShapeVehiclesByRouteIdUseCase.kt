package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse.RouteShapeVehicles
import com.yernazar.pidapplication.domain.repository.AppRepository

class GetRouteShapeVehiclesByRouteIdUseCase(private val appRepository: AppRepository) {
    suspend fun execute(routeId: String): RouteShapeVehicles? {

        return appRepository.getTripByRouteId(routeId)

    }
}