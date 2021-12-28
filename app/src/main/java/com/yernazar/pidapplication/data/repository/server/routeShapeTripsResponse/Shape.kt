package com.yernazar.pidapplication.data.repository.server.routeShapeTripsResponse

data class Shape(
    val distTraveled: Double,
    val lat: Double,
    val lon: Double,
    val uid: ShapeUid
)