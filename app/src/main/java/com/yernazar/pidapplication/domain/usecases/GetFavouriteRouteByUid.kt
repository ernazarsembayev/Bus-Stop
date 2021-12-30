package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.domain.repository.AppRepository

class GetFavouriteRouteByUid(private val appRepository: AppRepository) {

    suspend fun execute(routeUid: String): Route? {

        return appRepository.getFavouriteRouteById(routeUid)

    }

}