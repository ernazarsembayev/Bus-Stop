package com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse

import com.yernazar.pidapplication.data.repository.model.Route

data class RouteShapeVehicles(
    val route: Route,
    val routeShape: List<Shape>?,
    val vehicles: List<Vehicle>?
)