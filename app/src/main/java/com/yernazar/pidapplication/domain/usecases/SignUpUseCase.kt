package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.UserSignUp
import com.yernazar.pidapplication.domain.repository.AppRepository

class SignUpUseCase(private val appRepository: AppRepository) {

    suspend fun execute(userSignUp: UserSignUp): Boolean {

        return appRepository.signUp(userSignUp)

    }

}