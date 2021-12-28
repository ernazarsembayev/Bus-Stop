package com.yernazar.pidapplication.domain.repository

import com.yernazar.pidapplication.data.repository.server.routeShapeTripsResponse.RouteShapeVehicles
import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.data.repository.model.ShapeOld
import com.yernazar.pidapplication.data.repository.model.Stop

interface AppRepository {

    suspend fun getRouteByNameLike(routeName: String): List<Route>?

    suspend fun getRouteById(routeId: String): Route?

    suspend fun getTripByRouteId(routeId: String): RouteShapeVehicles?

    suspend fun getShapesById(shapeId: String): List<ShapeOld>

    suspend fun getAllStops(): List<Stop>

    suspend fun getRouteNextArrive(stopUid: String): List<Route>

}