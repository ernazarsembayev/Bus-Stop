package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.domain.repository.AppRepository

class DeleteFavouriteRoute(private val appRepository: AppRepository) {

    suspend fun execute(route: Route) {

        return appRepository.deleteFavouriteRoute(route)

    }

}