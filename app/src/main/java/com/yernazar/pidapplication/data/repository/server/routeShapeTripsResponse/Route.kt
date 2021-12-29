package com.yernazar.pidapplication.data.repository.server.routeShapeTripsResponse

data class Route(
    val agency: String,
    val color: String,
    val desc: String,
    val id: String,
    val longName: String,
    val night: Boolean,
    val shapeId: String,
    val shortName: String,
    val textColor: String,
    val type: String,
    val url: String
)