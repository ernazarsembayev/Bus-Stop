package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.domain.repository.AppRepository
import org.jguniverse.pidapplicationgm.repo.model.Route
import org.jguniverse.pidapplicationgm.repo.model.Trip

class GetTripByRouteIdUseCase(private val appRepository: AppRepository) {
    suspend fun execute(routeId: String): Trip? {

        return appRepository.getTripByRouteId(routeId)

    }
}