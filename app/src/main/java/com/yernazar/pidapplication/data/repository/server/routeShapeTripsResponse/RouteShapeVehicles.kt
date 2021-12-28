package com.yernazar.pidapplication.data.repository.server.routeShapeTripsResponse

import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.data.repository.model.Vehicle

data class RouteShapeVehicles(
    val route: Route,
    val routeShape: List<Shape>,
    val trips: List<Vehicle>
)