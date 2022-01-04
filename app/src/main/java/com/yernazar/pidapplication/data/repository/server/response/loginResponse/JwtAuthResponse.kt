package com.yernazar.pidapplication.data.repository.server.response.loginResponse

data class JwtAuthResponse(
    val accessToken: String,
    val tokenType: String
)