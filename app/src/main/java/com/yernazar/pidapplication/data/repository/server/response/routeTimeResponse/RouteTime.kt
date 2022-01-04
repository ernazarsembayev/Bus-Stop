package com.yernazar.pidapplication.data.repository.server.response.routeTimeResponse

import com.yernazar.pidapplication.data.repository.model.Route

data class RouteTime(
    val delayMin: Int,
    val expectedArrival: String,
    val route: Route
)