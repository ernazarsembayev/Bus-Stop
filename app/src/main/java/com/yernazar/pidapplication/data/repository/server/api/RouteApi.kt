package com.yernazar.pidapplication.data.repository.server.api

import com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse.RouteShapeVehicles
import com.yernazar.pidapplication.utils.config.Config
import com.yernazar.pidapplication.data.repository.model.Route
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RouteApi {

    @GET("{id}")
    suspend fun getById(@Path("id") id: String) : Response<Route?>

    // get trips for given route
    @GET("{id}/trips")
    suspend fun getTrips(@Path("id") routeId: String) : Response<RouteShapeVehicles>

    @GET("search/{name}")
    suspend fun getByNameLike(@Path("name") name: String) : Response<List<Route>>

    companion object {
        private var BASE_URL = Config.BASE_URL + "api/route/"

        fun create() : RouteApi {

            val retrofit = BaseApi.createRetrofit(BASE_URL)

            return retrofit.create(RouteApi::class.java)
        }
    }
}