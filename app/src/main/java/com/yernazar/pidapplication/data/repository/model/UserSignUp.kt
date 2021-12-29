package com.yernazar.pidapplication.data.repository.model

data class UserSignUp(
    var name: String,
    var username: String,
    var password: String,
    var email: String) {

    fun toUserLogin() = UserSignIn (

        usernameOrEmail = username,
        password = password
    )
}
