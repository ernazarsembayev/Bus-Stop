package com.yernazar.pidapplication.domain.usecases

import com.yernazar.pidapplication.data.repository.model.UserSignIn
import com.yernazar.pidapplication.data.repository.server.response.tokenResponse.Token
import com.yernazar.pidapplication.domain.repository.AppRepository

class SignInUseCase(private val appRepository: AppRepository) {

    suspend fun execute(userSignIn: UserSignIn): Token {

        return appRepository.signIn(userSignIn)

    }

}