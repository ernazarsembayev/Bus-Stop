package com.yernazar.pidapplication.data.repository.server.loginResponse

data class LoginResponse(
    val jwtAuthResponse: JwtAuthResponse,
    val user: User
)