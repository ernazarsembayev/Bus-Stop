package com.yernazar.pidapplication.data.repository.server.api

import com.yernazar.pidapplication.utils.config.Config
import com.yernazar.pidapplication.data.repository.model.GeoPoint
import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.data.repository.model.Stop
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface StopApi {

    @GET("stop/{id}")
    fun getById(@Path("id") id: Long) : Call<Stop>

    @GET("stop/")
    suspend fun getAllStops() : Response<List<Stop>>

    @GET("stop/{name}")
    fun getByNameLike(@Path("name") name: String) : Call<Set<Stop>>

    // get routes for given stop
    @GET("stop/{uid}/routes")
    suspend fun getRoutes(@Path("uid") uid: String) : Response<List<Route>>

    // get Stops in given area restricted by 2 points
    @GET("stop/{p1}/{p2}")
    fun getInArea(@Path("id") p1: GeoPoint, @Path("id") p2: GeoPoint) : Call<Set<Stop>>

    companion object {
        private var BASE_URL = Config.BASE_URL + "api/"

        fun create() : StopApi {

            val retrofit = BaseApi.createRetrofit(BASE_URL)

            return retrofit.create(StopApi::class.java)
        }
    }
}