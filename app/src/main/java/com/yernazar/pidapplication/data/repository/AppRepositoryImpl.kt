package com.yernazar.pidapplication.data.repository

import android.util.Log
import com.yernazar.pidapplication.data.repository.database.AppDatabase
import com.yernazar.pidapplication.data.repository.model.*
import com.yernazar.pidapplication.data.repository.server.ServerCommunicator
import com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse.RouteShapeVehicles
import com.yernazar.pidapplication.domain.repository.AppRepository
import com.yernazar.pidapplication.data.repository.server.response.tokenResponse.Token
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

    override suspend fun getRouteNextArrive(stopUid: String): List<Route> {

        val routeResponse = serverCommunicator.getRouteNextArrive(stopUid)
        if (routeResponse.isSuccessful)
            routeResponse.body()?.let {
                return it
            }
        return emptyList()
    }

    override suspend fun signIn(userSignIn: UserSignIn): Token {

        val authResponse = serverCommunicator.signIn(userSignIn = userSignIn)
        if (authResponse.isSuccessful)
            authResponse.body()?.let {
                return it
            }
        return Token("", "invalid")
    }


    override suspend fun signUp(userSignUp: UserSignUp): Boolean {

        val authResponse = serverCommunicator.signUp(userSignUp = userSignUp)
        if (authResponse.isSuccessful)
                return true
        return false
    }

}