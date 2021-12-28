package com.yernazar.pidapplication.data.repository.server.api

import com.yernazar.pidapplication.data.repository.model.*
import com.yernazar.pidapplication.utils.config.Config
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface TripApi {

    @GET("/{id}")
    fun getById(@Path("id") id: Long) : Call<Route>

    @GET("/{routeId}")
    suspend fun getByRouteId(@Path("routeId") routeId: String) : Response<Trip?>

    // get vehicles for given trip
    @GET("/{id}/vehicles")
    fun getVehicles(@Path("id") id: Long) : Call<Set<Vehicle>>

    // get geoshape i.e. set of geopoints to represent path(shape)
    @GET("/{id}/shape")
    fun getShape(@Path("id") id: Long) : Call<ShapeOld>

    // get current position
    @GET("/{id}/position")
    fun getPosition(@Path("id") id: Long) : Call<Position>


    companion object {
        private var BASE_URL = Config.BASE_URL + "api/trip/"

        fun create() : TripApi {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(TripApi::class.java)
        }
    }
}