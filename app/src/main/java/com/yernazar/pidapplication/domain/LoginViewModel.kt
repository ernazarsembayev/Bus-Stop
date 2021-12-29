package com.yernazar.pidapplication.domain

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.data.repository.model.UserSignIn
import com.yernazar.pidapplication.data.repository.model.UserSignUp
import com.yernazar.pidapplication.data.repository.server.response.tokenResponse.Token
import com.yernazar.pidapplication.domain.usecase.SignInUseCase
import com.yernazar.pidapplication.domain.usecase.SignUpUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val signInUseCase: SignInUseCase by inject()
    private val signUpUseCase: SignUpUseCase by inject()

    private val _liveDataToken = MutableLiveData<Token>()
    private val _liveDataShowSignUp = MutableLiveData<Unit>()
    private val _liveDataShowLogIn = MutableLiveData<Unit>()

    val liveDataToken: LiveData<Token> = _liveDataToken
    val liveDataShowSignUp: LiveData<Unit> = _liveDataShowSignUp
    val liveDataShowLogIn: LiveData<Unit> = _liveDataShowLogIn


    fun onSignIn(userSignIn: UserSignIn){
        CoroutineScope(Dispatchers.Default).launch {
            val result = signInUseCase.execute(userSignIn)
            Log.e("token", result.accessToken)
            CoroutineScope(Dispatchers.Main).launch {
                if (result.tokenType != "invalid")
                    _liveDataToken.value = result
                else {
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