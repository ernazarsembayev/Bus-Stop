package com.yernazar.pidapplication.data.repository.server

import com.yernazar.pidapplication.data.repository.model.*
import com.yernazar.pidapplication.data.repository.server.api.RouteApi
import com.yernazar.pidapplication.data.repository.server.api.ShapeApi
import com.yernazar.pidapplication.data.repository.server.api.StopApi
import com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse.RouteShapeVehicles
import com.yernazar.pidapplication.data.repository.server.api.AuthApi
import com.yernazar.pidapplication.data.repository.server.response.tokenResponse.Token
import retrofit2.Response

class ServerCommunicator {

    companion object {
        private const val DEFAULT_TIMEOUT = 10
        private const val DEFAULT_RETRY_ATTEMPTS = 4L
    }

    suspend fun getRouteByNameLike(routeName: String): Response<List<Route>> {
        val routeApi = RouteApi.create()
        return routeApi.getByNameLike(routeName)
    }

    suspend fun getRouteById(routeId: String): Response<Route?> {
        val routeApi = RouteApi.create()
        return routeApi.getById(routeId)
    }

    suspend fun getTripByRouteId(routeId: String): Response<RouteShapeVehicles> {
        val routeApi = RouteApi.create()
        return routeApi.getTrips(routeId)
    }

    suspend fun getShapesById(shapeId: String): Response<List<ShapeOld>> {
        val shapeApi = ShapeApi.create()
        return shapeApi.getById(shapeId)
    }

    suspend fun getAllStops(): Response<List<Stop>> {
        val stopApi = StopApi.create()
        return stopApi.getAllStops()
    }

    suspend fun getRouteNextArrive(stopUid: String): Response<List<Route>> {
        val stopApi = StopApi.create()
        return stopApi.getRoutes(stopUid)
    }

    suspend fun signIn(userSignIn: UserSignIn): Response<Token> {
        val authApi = AuthApi.create()
        return authApi.signIn(userSignIn)
    }

    suspend fun signUp(userSignUp: UserSignUp): Response<Unit> {
        val authApi = AuthApi.create()
        return authApi.signUp(userSignUp)
    }


}