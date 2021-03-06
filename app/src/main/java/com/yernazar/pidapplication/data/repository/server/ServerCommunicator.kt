package com.yernazar.pidapplication.data.repository.server

import com.yernazar.pidapplication.data.repository.model.*
import com.yernazar.pidapplication.data.repository.server.api.*
import com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse.RouteShapeVehicles
import com.yernazar.pidapplication.data.repository.server.response.loginResponse.LoginResponse
import com.yernazar.pidapplication.data.repository.server.response.routeTimeResponse.RouteTime
import retrofit2.Response

class ServerCommunicator {

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

    suspend fun getRouteNextArrive(stopUid: String): Response<List<RouteTime>> {
        val stopApi = StopApi.create()
        return stopApi.getRoutes(stopUid)
    }

    suspend fun signIn(userSignIn: UserSignIn): Response<LoginResponse> {
        val authApi = AuthApi.create()
        return authApi.signIn(userSignIn)
    }

    suspend fun signUp(userSignUp: UserSignUp): Response<Unit> {
        val authApi = AuthApi.create()
        return authApi.signUp(userSignUp)
    }

    suspend fun postFavouriteRoute(routeUid: String, token: String): Response<Unit> {
        val userApi = UserApi.create(token)
        return userApi.postFavouriteRoute(routeUid)
    }

    suspend fun deleteFavouriteRoute(routeUid: String, token: String): Response<Unit> {
        val userApi = UserApi.create(token)
        return userApi.deleteFavouriteRoute(routeUid)
    }

    suspend fun postFavouriteTrip(tripUid: String, token: String): Response<Unit> {
        val userApi = UserApi.create(token)
        return userApi.postFavouriteTrip(tripUid)
    }

    suspend fun deleteFavouriteTrip(tripUid: String, token: String): Response<Unit> {
        val userApi = UserApi.create(token)
        return userApi.deleteFavouriteTrip(tripUid)
    }

}