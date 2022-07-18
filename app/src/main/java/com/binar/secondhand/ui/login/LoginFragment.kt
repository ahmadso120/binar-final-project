package com.binar.secondhand.ui.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.LoginRequest
import com.binar.secondhand.databinding.FragmentLoginBinding
import com.binar.secondhand.storage.UserLoggedIn
import com.binar.secondhand.utils.ui.focusAndShowKeyboard
import com.binar.secondhand.utils.ui.hideKeyboard
import com.binar.secondhand.utils.ui.showShortSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    override var bottomNavigationViewVisibility = View.GONE

    private val binding: FragmentLoginBinding by viewBinding()

    private val viewModel by viewModel<LoginViewModel>()

    private lateinit var savedStateHandle: SavedStateHandle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle[LOGIN_SUCCESSFUL] = false

        binding.apply {
            emailEdt.focusAndShowKeyboard()
            loginBtn.setOnClickListener {
                requireActivity().currentFocus?.hideKeyboard()
                val loginRequest = LoginRequest(
                    emailEdt.text.toString().trim(),
                    passwordEdt.text.toString().trim()
                )
                viewModel.doLoginRequest(loginRequest)
                loginBtn.isEnabled = false
            }
            registerTv.setOnClickListener {
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
            toolbar.setNavigationOnClickListener {
                navController.navigateUp()
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
                    savedStateHandle[LOGIN_SUCCESSFUL] = true
                    navController.popBackStack()
                }
            }
        }
    }

    companion object {
        const val LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL"
    }
}