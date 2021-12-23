package com.yernazar.pidapplication.data.repository.server

import com.yernazar.pidapplication.data.repository.server.api.RouteApi
import com.yernazar.pidapplication.data.repository.server.api.ShapeApi
import com.yernazar.pidapplication.data.repository.server.api.StopApi
import com.yernazar.pidapplication.data.repository.server.api.TripApi
import org.jguniverse.pidapplicationgm.repo.model.Route
import org.jguniverse.pidapplicationgm.repo.model.Shape
import org.jguniverse.pidapplicationgm.repo.model.Stop
import org.jguniverse.pidapplicationgm.repo.model.Trip
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

    suspend fun getTripByRouteId(routeId: String): Response<List<Trip>> {
        val routeApi = RouteApi.create()
        return routeApi.getTrips(routeId)
    }

    suspend fun getShapesById(shapeId: String): Response<List<Shape>> {
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

}