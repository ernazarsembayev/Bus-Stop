package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.domain.repository.AppRepository
import com.yernazar.pidapplication.data.repository.model.Route

class GetRouteByNameLikeUseCase(private val appRepository: AppRepository) {

    suspend fun execute(requestText: String): List<Route>? {

        return appRepository.getRouteByNameLike(requestText)

    }

}