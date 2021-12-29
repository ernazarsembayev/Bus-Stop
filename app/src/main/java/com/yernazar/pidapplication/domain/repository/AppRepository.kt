package com.yernazar.pidapplication.domain.repository

import com.yernazar.pidapplication.data.repository.model.*
import com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse.RouteShapeVehicles
import com.yernazar.pidapplication.data.repository.server.response.tokenResponse.Token

interface AppRepository {

    suspend fun getRouteByNameLike(routeName: String): List<Route>?

    suspend fun getRouteById(routeId: String): Route?

    suspend fun getTripByRouteId(routeId: String): RouteShapeVehicles?

    suspend fun getShapesById(shapeId: String): List<ShapeOld>

    suspend fun getAllStops(): List<Stop>

    suspend fun getRouteNextArrive(stopUid: String): List<Route>

    suspend fun signIn(userSignIn: UserSignIn): Token

    suspend fun signUp(userSignUp: UserSignUp): Boolean

}