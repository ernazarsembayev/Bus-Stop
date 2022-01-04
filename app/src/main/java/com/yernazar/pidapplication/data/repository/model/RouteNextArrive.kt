package com.yernazar.pidapplication.data.repository.model

data class RouteNextArrive(
    val uid: String,
    val longName: String,
    val shortName: String,
    val type: String,
    val url: String,
    val nextArrive: Long,
    val shapeId: String,
    val isNight: Boolean
)