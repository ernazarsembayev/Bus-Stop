package com.yernazar.pidapplication.data.repository.server.loginResponse

data class JwtAuthResponse(
    val accessToken: String,
    val tokenType: String
)