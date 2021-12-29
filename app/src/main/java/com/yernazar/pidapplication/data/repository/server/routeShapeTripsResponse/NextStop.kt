package com.yernazar.pidapplication.data.repository.server.routeShapeTripsResponse

data class NextStop(
    val id: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val parentStation: String,
    val weelchair: Int,
    val zoneId: String
)