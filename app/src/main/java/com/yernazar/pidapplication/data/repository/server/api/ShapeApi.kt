package com.yernazar.pidapplication.data.repository.server.api

import com.yernazar.pidapplication.utils.config.Config
import com.yernazar.pidapplication.data.repository.model.ShapeOld
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ShapeApi {

    @GET("{id}")
    suspend fun getById(@Path("id") shapeId: String) : Response<List<ShapeOld>>

    companion object {
        private var BASE_URL = Config.BASE_URL + "api/shape/"

        fun create() : ShapeApi {

            val retrofit = BaseApi.createRetrofit(BASE_URL, null)

            return retrofit.create(ShapeApi::class.java)
        }
    }
}