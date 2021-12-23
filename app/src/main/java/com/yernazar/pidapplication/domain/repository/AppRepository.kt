package com.yernazar.pidapplication.domain.repository

import com.yernazar.pidapplication.data.repository.model.RouteAndNextArrive
import org.jguniverse.pidapplicationgm.repo.model.Route
import org.jguniverse.pidapplicationgm.repo.model.Shape
import org.jguniverse.pidapplicationgm.repo.model.Stop
import org.jguniverse.pidapplicationgm.repo.model.Trip

interface AppRepository {

    suspend fun getRouteByNameLike(routeName: String): List<Route>?

    suspend fun getRouteById(routeId: String): Route?

    suspend fun getTripByRouteId(routeId: String): Trip?

    suspend fun getShapesById(shapeId: String): List<Shape>

    suspend fun getAllStops(): List<Stop>

    suspend fun getRouteNextArrive(stopUid: String): List<Route>

}