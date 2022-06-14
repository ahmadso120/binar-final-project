package com.binar.secondhand.ui.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.LoginRequest
import com.binar.secondhand.databinding.FragmentLoginBinding
import com.binar.secondhand.storage.UserLoggedIn
import com.binar.secondhand.utils.showShortSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    override var bottomNavigationViewVisibility = View.GONE

    private val binding: FragmentLoginBinding by viewBinding()

    private val viewModel by viewModel<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginBtn.setOnClickListener {
                val loginRequest = LoginRequest(
                    emailEdt.text.toString().trim(),
                    passwordEdt.text.toString().trim()
                )
                viewModel.doLoginRequest(loginRequest)
                loginBtn.isEnabled = false
            }
        }

        observeUi()
    }

    private fun observeUi() {
        viewModel.login.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Error -> {
                    it.error?.let { err ->
                        view?.showShortSnackbar(err)
                    }

                    binding.loginBtn.text = "Masuk"
                    binding.loginBtn.isEnabled = true
                }
                Result.Loading -> {
                    binding.loginBtn.text = "Loading...."
                }
                is Result.Success -> {
                    val userLoggedIn = UserLoggedIn(
                        it.data.accessToken,
                        it.data.name,
                        it.data.email
                    )
                    viewModel.setUserLoggedIn(userLoggedIn)
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }
}