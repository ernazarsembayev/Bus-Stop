package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.domain.repository.AppRepository
import com.yernazar.pidapplication.data.repository.model.Route

class GetRouteByIdUseCase(private val appRepository: AppRepository) {

    suspend fun execute(routeId: String): Route? {

        return appRepository.getRouteById(routeId)

    }

}