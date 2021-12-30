package com.yernazar.pidapplication.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.databinding.ActivityLoginBinding
import com.yernazar.pidapplication.domain.LoginViewModel
import com.yernazar.pidapplication.presentation.fragment.LoginFragment
import com.yernazar.pidapplication.presentation.fragment.SignUpFragment
import com.yernazar.pidapplication.utils.config.Config.SHARED_PREFERENCES
import com.yernazar.pidapplication.utils.config.Config.SP_TOKEN
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModel<LoginViewModel>()

    private val loginFragment: LoginFragment by inject()
    private val mSignUpFragment: SignUpFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.liveDataToken.observe(this, {

            val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(SP_TOKEN, it.accessToken)
            editor.apply()

            val mapsIntent = Intent(this, MapsActivity::class.java)
            startActivity(mapsIntent)
        })

        viewModel.liveDataShowSignUp.observe(this, {
            showFragment(mSignUpFragment)
        })

        viewModel.liveDataShowLogIn.observe(this, {
            showFragment(loginFragment)
        })

        showFragment(loginFragment)

    }

    private fun <T : Fragment> showFragment(fragment: T) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container, fragment,
                fragment.toString())
            .commit()
    }
}