package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.domain.repository.AppRepository

class DeleteFavouriteRouteUseCase(private val appRepository: AppRepository) {

    suspend fun execute(route: Route, token: String) {

        return appRepository.deleteFavouriteRoute(route, token)

    }

}