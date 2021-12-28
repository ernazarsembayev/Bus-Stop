package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.data.repository.server.routeShapeTripsResponse.RouteShapeVehicles
import com.yernazar.pidapplication.domain.repository.AppRepository

class GetTripByRouteIdUseCase(private val appRepository: AppRepository) {
    suspend fun execute(routeId: String): RouteShapeVehicles? {

        return appRepository.getTripByRouteId(routeId)

    }
}