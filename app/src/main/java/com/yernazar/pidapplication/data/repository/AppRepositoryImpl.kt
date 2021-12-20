package com.yernazar.pidapplication.data.repository

import android.util.Log
import com.yernazar.pidapplication.data.repository.database.AppDatabase
import com.yernazar.pidapplication.data.repository.model.RouteAndNextArrive
import com.yernazar.pidapplication.data.repository.server.ServerCommunicator
import com.yernazar.pidapplication.domain.repository.AppRepository
import org.jguniverse.pidapplicationgm.repo.model.Route
import org.jguniverse.pidapplicationgm.repo.model.Shape
import org.jguniverse.pidapplicationgm.repo.model.Stop
import org.jguniverse.pidapplicationgm.repo.model.Trip

class AppRepositoryImpl(private val serverCommunicator: ServerCommunicator, private val database: AppDatabase) : AppRepository {

    override suspend fun getRouteByNameLike(routeName: String): List<Route>? {
        val routeResponse = serverCommunicator.getRouteByNameLike(routeName)
        if (routeResponse.isSuccessful)
            routeResponse.body()?.let {
                Log.e("routeResponse.body",routeResponse.body().toString())
                return routeResponse.body()
            }
        return database.routeDao().getByNameLike(routeName)
    }

    override suspend fun getRouteById(routeId: String): Route? {
        val routeResponse = serverCommunicator.getRouteById(routeId)
        if (routeResponse.isSuccessful)
            routeResponse.body()?.let {
                return routeResponse.body()
            }
        return database.routeDao().getById(routeId)
    }

    override suspend fun getTripByRouteId(routeId: String): Trip? {
        val tripResponse = serverCommunicator.getTripByRouteId(routeId)
        if (tripResponse.isSuccessful)
            tripResponse.body()?.let {
                return it
            }
        return database.tripDao().getByRouteId(routeId)
    }

    override suspend fun getShapesById(shapeId: String): List<Shape> {
        val shapesResponse = serverCommunicator.getShapesById(shapeId)
        if (shapesResponse.isSuccessful)
            shapesResponse.body()?.let {
                return it
            }
        return database.shapeDao().getById(shapeId)
    }

    override suspend fun getAllStops(): List<Stop> {
        val stopsResponse = serverCommunicator.getAllStops()
        if (stopsResponse.isSuccessful)
            stopsResponse.body()?.let {
                return it
            }
        return database.stopDao().getAll()
    }

    override suspend fun getRouteNextArrive(stopUid: String): List<RouteAndNextArrive> {
//        val routeResponse = serverCommunicator.getRouteNextArrive()
//        if (routeResponse.isSuccessful)
//            routeResponse.body()?.let {
//                return it
//            }
        return database.routeDao().getRouteNextArrive(stopUid)
    }

}