package com.yernazar.pidapplication.domain.repository

import com.yernazar.pidapplication.data.repository.model.*
import com.yernazar.pidapplication.data.repository.server.loginResponse.LoginResponse
import com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse.RouteShapeVehicles

interface AppRepository {

    suspend fun getRouteByNameLike(routeName: String): List<Route>?

    suspend fun getRouteById(routeId: String): Route?

    suspend fun getTripByRouteId(routeId: String): RouteShapeVehicles?

    suspend fun getShapesById(shapeId: String): List<ShapeOld>

    suspend fun getAllStops(): List<Stop>

    suspend fun getRouteNextArrive(stopUid: String): List<Route>

    suspend fun signIn(userSignIn: UserSignIn): LoginResponse

    suspend fun signUp(userSignUp: UserSignUp): Boolean

    suspend fun getFavouriteRoutes(): List<Route>

    suspend fun getFavouriteRouteById(routeUid: String): Route?

    suspend fun saveFavouriteRoute(route: Route)

    suspend fun saveFavouriteRoutes(routes: List<Route>)

    suspend fun deleteFavouriteRoute(route: Route)

    suspend fun saveFavouriteTrip(trip: Trip)

    suspend fun deleteFavouriteTrip(trip: Trip)

    suspend fun saveFavouriteTrips(trips: List<Trip>)

    suspend fun clearFavourites()

}