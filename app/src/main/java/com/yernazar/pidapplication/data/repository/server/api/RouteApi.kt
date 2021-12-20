package com.yernazar.pidapplication.data.repository.server.api

import com.yernazar.pidapplication.utils.config.Config
import org.jguniverse.pidapplicationgm.repo.model.Route
import org.jguniverse.pidapplicationgm.repo.model.Trip
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RouteApi {

    @GET("/{id}")
    suspend fun getById(@Path("id") id: String) : Response<Route?>

    // get trips for given route
    @GET("/{id}/trips")
    fun getTrips(@Path("id") id: Long) : Call<Set<Trip>>

    @GET("/{name}")
    suspend fun getByNameLike(@Path("name") name: String) : Response<List<Route>>

    companion object {
        private var BASE_URL = Config.BASE_URL + "api/route/"

        fun create() : RouteApi {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(RouteApi::class.java)
        }
    }
}