package com.yernazar.pidapplication.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yernazar.pidapplication.data.repository.model.UserSignIn
import com.yernazar.pidapplication.databinding.FragmentLoginBinding
import com.yernazar.pidapplication.domain.LoginSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel by sharedViewModel<LoginSharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {

            val username = binding.editEmail.editText?.text.toString()
            val password = binding.editPassword.editText?.text.toString()

            if ( username.isNotBlank() && password.isNotBlank() ) {
                val user = UserSignIn(username, password)

                viewModel.onSignIn(user)
            }

        }

        binding.buttonRegister.setOnClickListener{
            viewModel.onRegisterClick()
        }

    }
}