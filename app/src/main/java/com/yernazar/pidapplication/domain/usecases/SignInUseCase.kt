package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.UserSignIn
import com.yernazar.pidapplication.data.repository.server.response.loginResponse.LoginResponse
import com.yernazar.pidapplication.domain.repository.AppRepository

class SignInUseCase(private val appRepository: AppRepository) {

    suspend fun execute(userSignIn: UserSignIn): LoginResponse {

        return appRepository.signIn(userSignIn)

    }

}