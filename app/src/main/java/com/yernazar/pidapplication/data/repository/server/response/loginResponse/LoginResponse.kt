package com.yernazar.pidapplication.data.repository.server.response.loginResponse

data class LoginResponse(
    val jwtAuthResponse: JwtAuthResponse,
    val user: User
)