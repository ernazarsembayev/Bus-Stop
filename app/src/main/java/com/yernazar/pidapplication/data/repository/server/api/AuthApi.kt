package com.yernazar.pidapplication.data.repository.server.api

import com.yernazar.pidapplication.data.repository.model.UserSignIn
import com.yernazar.pidapplication.data.repository.model.UserSignUp
import com.yernazar.pidapplication.data.repository.server.response.tokenResponse.Token
import com.yernazar.pidapplication.utils.config.Config
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AuthApi {

    @POST("signin")
    suspend fun signIn(@Body userSignIn: UserSignIn): Response<Token>

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