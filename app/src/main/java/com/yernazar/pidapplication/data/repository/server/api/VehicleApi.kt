package com.yernazar.pidapplication.data.repository.server.api

import com.yernazar.pidapplication.utils.config.Config
import org.jguniverse.pidapplicationgm.repo.model.Route
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleApi {

    @GET("/{id}")
    fun getById(@Path("id") id: Long) : Call<Route>

    companion object {
        private var BASE_URL = Config.BASE_URL + "api/vehicle/"

        fun create() : StopApi {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(StopApi::class.java)
        }
    }
}