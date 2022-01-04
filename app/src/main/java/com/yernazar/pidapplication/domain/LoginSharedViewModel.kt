package com.yernazar.pidapplication.domain

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.data.repository.model.UserSignIn
import com.yernazar.pidapplication.data.repository.model.UserSignUp
import com.yernazar.pidapplication.data.repository.server.response.loginResponse.JwtAuthResponse
import com.yernazar.pidapplication.data.repository.server.response.loginResponse.LoginResponse
import com.yernazar.pidapplication.domain.usecases.SaveFavouriteRoutesUseCase
import com.yernazar.pidapplication.domain.usecases.SaveFavouriteTripsUseCase
import com.yernazar.pidapplication.domain.usecases.SignInUseCase
import com.yernazar.pidapplication.domain.usecases.SignUpUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import java.lang.Exception

class LoginSharedViewModel(application: Application) : BaseViewModel(application) {

    private val signInUseCase: SignInUseCase by inject()
    private val signUpUseCase: SignUpUseCase by inject()
    private val saveFavouriteRoutesUseCase: SaveFavouriteRoutesUseCase by inject()
    private val saveFavouriteTripsUseCase: SaveFavouriteTripsUseCase by inject()

    private val _liveDataToken = MutableLiveData<LoginResponse>()
    private val _liveDataShowSignUp = MutableLiveData<Unit>()
    private val _liveDataShowLogIn = MutableLiveData<Unit>()

    val liveDataToken: LiveData<LoginResponse> = _liveDataToken
    val liveDataShowSignUp: LiveData<Unit> = _liveDataShowSignUp
    val liveDataShowLogIn: LiveData<Unit> = _liveDataShowLogIn


    fun onSignIn(userSignIn: UserSignIn){
        CoroutineScope(Dispatchers.Default).launch {
            try {

                val result = signInUseCase.execute(userSignIn)

                if (result.user.favouriteRoutes.isNotEmpty()) {
                    saveFavouriteRoutesUseCase.execute(result.user.favouriteRoutes)
                }
                if (result.user.favouriteTrips.isNotEmpty()) {
                    saveFavouriteTripsUseCase.execute(result.user.favouriteTrips)
                }

                CoroutineScope(Dispatchers.Main).launch {
                        _liveDataToken.value = result
                }

            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(getApplication(), R.string.login_error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun onSignUp(userSignUp: UserSignUp){
        CoroutineScope(Dispatchers.Default).launch {
            val result = signUpUseCase.execute(userSignUp)
            if (result)
                onSignIn(userSignUp.toUserLogin())
            else Log.e("Error sign up", "dsfg")
        }
    }

    fun onLoginClick() {
        _liveDataShowLogIn.value = Unit
    }

    fun onRegisterClick() {
        _liveDataShowSignUp.value = Unit
    }

}