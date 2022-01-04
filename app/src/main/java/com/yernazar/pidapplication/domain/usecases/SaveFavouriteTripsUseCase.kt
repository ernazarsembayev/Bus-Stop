package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.Trip
import com.yernazar.pidapplication.domain.repository.AppRepository

class SaveFavouriteTripsUseCase(private val appRepository: AppRepository) {

    suspend fun execute(trips: List<Trip>) {

        return appRepository.saveFavouriteTrips(trips)

    }

}