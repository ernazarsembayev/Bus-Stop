package com.yernazar.pidapplication.data.repository.server.loginResponse

import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.data.repository.model.Trip

data class User(
    val email: String,
    val favouriteRoutes: List<Route>,
    val favouriteTrips: List<Trip>,
    val id: Int,
    val name: String,
    val password: String,
    val role: String,
    val username: String
)