package com.yernazar.pidapplication.data.repository

import android.util.Log
import com.yernazar.pidapplication.data.repository.database.AppDatabase
import com.yernazar.pidapplication.data.repository.model.*
import com.yernazar.pidapplication.data.repository.server.ServerCommunicator
import com.yernazar.pidapplication.data.repository.server.response.loginResponse.LoginResponse
import com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse.RouteShapeVehicles
import com.yernazar.pidapplication.data.repository.server.response.routeTimeResponse.RouteTime
import com.yernazar.pidapplication.domain.repository.AppRepository
import java.lang.Exception

class AppRepositoryImpl(private val serverCommunicator: ServerCommunicator, private val database: AppDatabase) : AppRepository {

    override suspend fun getRouteByNameLike(routeName: String): List<Route>? {
        val routeResponse = serverCommunicator.getRouteByNameLike(routeName)
        if (routeResponse.isSuccessful)
            routeResponse.body()?.let {
                Log.e("routeResponse.body",routeResponse.body().toString())
                return routeResponse.body()
            }
        return emptyList()
    }

    override suspend fun getRouteById(routeId: String): Route? {
        val routeResponse = serverCommunicator.getRouteById(routeId)
        if (routeResponse.isSuccessful)
            routeResponse.body()?.let {
                return routeResponse.body()
            }
        return null
    }

    override suspend fun getTripByRouteId(routeId: String): RouteShapeVehicles? {
        val tripResponse = serverCommunicator.getTripByRouteId(routeId)
        if (tripResponse.isSuccessful)
            tripResponse.body()?.let {
                return it
            }
        return null
    }

    override suspend fun getShapesById(shapeId: String): List<ShapeOld> {
        val shapesResponse = serverCommunicator.getShapesById(shapeId)
        if (shapesResponse.isSuccessful)
            shapesResponse.body()?.let {
                return it
            }
//        return database.shapeDao().getById(shapeId)
        return emptyList()
    }

    override suspend fun getAllStops(): List<Stop> {
        val stopsResponse = serverCommunicator.getAllStops()
        if (stopsResponse.isSuccessful)
            stopsResponse.body()?.let {
                return it
            }
        return emptyList()
    }

    override suspend fun getRouteNextArrive(stopUid: String): List<RouteTime> {

        val routeResponse = serverCommunicator.getRouteNextArrive(stopUid)
        if (routeResponse.isSuccessful)
            routeResponse.body()?.let {
                return it
            }
        return emptyList()
    }

    override suspend fun signIn(userSignIn: UserSignIn): LoginResponse {

        val authResponse = serverCommunicator.signIn(userSignIn = userSignIn)
        if (authResponse.isSuccessful)
            authResponse.body()?.let {
                return it
            }
        throw Exception()
    }


    override suspend fun signUp(userSignUp: UserSignUp): Boolean {

        val authResponse = serverCommunicator.signUp(userSignUp = userSignUp)
        if (authResponse.isSuccessful)
                return true
        return false
    }

    override suspend fun getFavouriteRoutes(): List<Route> {
        val favouriteRoutes = database.favouriteRoutesDao().getAll()
        favouriteRoutes?.let {
            return it
        }
        return emptyList()
    }

    override suspend fun getFavouriteRouteById(routeUid: String): Route? {
        val favouriteRoute = database.favouriteRoutesDao().getById(routeUid)
        favouriteRoute?.let {
            return it
        }
        return null
    }

    override suspend fun saveFavouriteRoute(route: Route, token: String) {
        serverCommunicator.postFavouriteRoute(routeUid = route.uid, token)
        database.favouriteRoutesDao().insert(route)
    }

    override suspend fun deleteFavouriteRoute(route: Route, token: String) {
        serverCommunicator.deleteFavouriteRoute(routeUid = route.uid, token)
        database.favouriteRoutesDao().delete(route)
    }

    override suspend fun saveFavouriteRoutes(routes: List<Route>) {
        database.favouriteRoutesDao().insertAll(routes)
    }

    override suspend fun getFavouriteTripById(tripUid: String): Trip? {
        return database.tripDao().getById(tripUid)
    }

    override suspend fun saveFavouriteTrips(trips: List<Trip>) {
        database.tripDao().insertAll(trips)
    }

    override suspend fun saveFavouriteTrip(trip: Trip, token: String) {
        serverCommunicator.postFavouriteTrip(tripUid = trip.uid, token)
        database.tripDao().insert(trip)
    }

    override suspend fun deleteFavouriteTrip(trip: Trip, token: String) {
        serverCommunicator.deleteFavouriteTrip(tripUid = trip.uid, token)
        database.tripDao().delete(trip)
    }

    override suspend fun clearFavourites() {
        database.favouriteRoutesDao().deleteAllFavouriteRoutes()
        database.tripDao().deleteAllTrips()
    }
}