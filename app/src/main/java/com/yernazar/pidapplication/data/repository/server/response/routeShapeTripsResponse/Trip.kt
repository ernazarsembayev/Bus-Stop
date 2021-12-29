package com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse

data class Trip(
    val uid: String,
    val route: Route,
    val direction: Int,
    val shapeId: String,
    val exceptional: Int,
    val wheelchair: Boolean,
    val headsign: String,
    val bikesAllowed: Boolean,
    val blockId: String,
)