package com.yernazar.pidapplication.data.repository.server.api

import com.yernazar.pidapplication.data.repository.model.UserSignIn
import com.yernazar.pidapplication.data.repository.model.UserSignUp
import com.yernazar.pidapplication.data.repository.server.loginResponse.LoginResponse
import com.yernazar.pidapplication.utils.config.Config
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("signin")
    suspend fun signIn(@Body userSignIn: UserSignIn): Response<LoginResponse>

    @POST("signup")
    suspend fun signUp(@Body userSignUp: UserSignUp) : Response<Unit>

    companion object {

        private var BASE_URL = Config.BASE_URL + "api/auth/"

        fun create() : AuthApi {

            val retrofit = BaseApi.createRetrofit(BASE_URL)

            return retrofit.create(AuthApi::class.java)

        }
    }
}