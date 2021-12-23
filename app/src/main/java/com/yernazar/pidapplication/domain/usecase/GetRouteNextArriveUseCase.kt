package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.data.repository.model.RouteAndNextArrive
import com.yernazar.pidapplication.domain.repository.AppRepository
import org.jguniverse.pidapplicationgm.repo.model.Route

class GetRouteNextArriveUseCase(private val appRepository: AppRepository) {

    suspend fun execute(stopUid: String): List<Route> {

        return appRepository.getRouteNextArrive(stopUid)

    }

}