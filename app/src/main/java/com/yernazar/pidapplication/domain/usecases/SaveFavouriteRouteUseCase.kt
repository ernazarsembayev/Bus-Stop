package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.domain.repository.AppRepository

class SaveFavouriteRouteUseCase(private val appRepository: AppRepository) {

    suspend fun execute(route: Route, token: String) {

        return appRepository.saveFavouriteRoute(route, token)

    }

}