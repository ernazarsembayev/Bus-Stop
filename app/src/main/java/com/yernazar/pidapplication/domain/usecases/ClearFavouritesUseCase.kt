package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.domain.repository.AppRepository

class ClearFavouritesUseCase(private val appRepository: AppRepository) {

    suspend fun execute() {

        return appRepository.clearFavourites()

    }

}