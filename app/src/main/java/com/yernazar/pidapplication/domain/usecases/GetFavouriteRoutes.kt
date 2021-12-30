package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.domain.repository.AppRepository

class GetFavouriteRoutes(private val appRepository: AppRepository) {

    suspend fun execute(): List<Route> {

        return appRepository.getFavouriteRoutes()

    }

}