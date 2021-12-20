package com.yernazar.pidapplication.data.repository.model

data class RouteAndNextArrive(
    val uid: String,
    val longName: String,
    val shortName: String,
    val type: String,
    val url: String,
    val nextArrive: Long,
    val isNight: Boolean
)