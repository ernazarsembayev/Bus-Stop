package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.domain.repository.AppRepository
import com.yernazar.pidapplication.data.repository.model.Route

class GetRouteByIdUseCase(private val appRepository: AppRepository) {

    suspend fun execute(routeId: String): Route? {

        return appRepository.getRouteById(routeId)

    }

}