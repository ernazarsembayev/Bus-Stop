package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.domain.repository.AppRepository
import org.jguniverse.pidapplicationgm.repo.model.Route

class GetRouteByIdUseCase(private val appRepository: AppRepository) {

    suspend fun execute(routeId: String): Route? {

        return appRepository.getRouteById(routeId)

    }

}