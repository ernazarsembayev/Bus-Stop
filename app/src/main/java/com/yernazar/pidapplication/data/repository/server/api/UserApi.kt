package com.yernazar.pidapplication.data.repository.server.api

import com.yernazar.pidapplication.data.repository.model.*
import com.yernazar.pidapplication.utils.config.Config
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @GET("/routes")
    fun getFavouriteRoutes() : Response<List<Route>>

    @GET("/trips")
    fun getFavouriteTrips() : Response<List<Route>>

    @POST("/routes/{routeId}")
    suspend fun postFavouriteRoute(@Path("routeId") routeId: String) : Response<Trip?>

    // get vehicles for given trip
    @DELETE("/routes/{routeId}")
    suspend fun deleteFavouriteRoute(@Path("routeId") routeId: String) : Response<Trip?>

    @POST("/trips/{tripId}")
    suspend fun postFavouriteTrip(@Path("tripId") tripId: String) : Response<Trip?>

    // get vehicles for given trip
    @DELETE("/trips/{tripId}")
    suspend fun deleteFavouriteTrip(@Path("tripId") tripId: String) : Response<Trip?>

    companion object {
        private var BASE_URL = Config.BASE_URL + "api/user/"

        fun create() : UserApi {

            val retrofit = BaseApi.createRetrofit(BASE_URL)
            return retrofit.create(UserApi::class.java)
        }
    }
}