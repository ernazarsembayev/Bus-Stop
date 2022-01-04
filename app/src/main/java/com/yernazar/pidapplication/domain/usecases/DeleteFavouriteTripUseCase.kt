package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.Trip
import com.yernazar.pidapplication.domain.repository.AppRepository

class DeleteFavouriteTripUseCase(private val appRepository: AppRepository) {

    suspend fun execute(trip: Trip, token: String) {

        return appRepository.deleteFavouriteTrip(trip, token)

    }

}