package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.data.repository.model.UserSignUp
import com.yernazar.pidapplication.domain.repository.AppRepository

class SignUpUseCase(private val appRepository: AppRepository) {

    suspend fun execute(userSignUp: UserSignUp): Boolean {

        return appRepository.signUp(userSignUp)

    }

}