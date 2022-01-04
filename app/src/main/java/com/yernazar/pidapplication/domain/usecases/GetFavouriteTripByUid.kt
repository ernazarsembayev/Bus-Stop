package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.Trip
import com.yernazar.pidapplication.domain.repository.AppRepository

class GetFavouriteTripByUid(private val appRepository: AppRepository) {

    suspend fun execute(tripUid: String): Trip? {

        return appRepository.getFavouriteTripById(tripUid)

    }

}