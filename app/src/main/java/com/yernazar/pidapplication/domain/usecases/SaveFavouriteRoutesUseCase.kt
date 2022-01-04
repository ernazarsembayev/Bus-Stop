package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.domain.repository.AppRepository

class SaveFavouriteRoutesUseCase(private val appRepository: AppRepository) {

    suspend fun execute(routes: List<Route>) {

        return appRepository.saveFavouriteRoutes(routes)

    }

}