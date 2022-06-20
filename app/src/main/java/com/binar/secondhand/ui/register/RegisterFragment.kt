package com.binar.secondhand.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.RegisterRequest
import com.binar.secondhand.databinding.FragmentRegisterBinding
import com.binar.secondhand.utils.logd
import com.binar.secondhand.utils.showShortSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : BaseFragment(R.layout.fragment_register) {
    private val binding: FragmentRegisterBinding by viewBinding()
    override var bottomNavigationViewVisibility = View.GONE
    private val viewModel by viewModel<RegisterViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            registerBtn.setOnClickListener {
                val phoneNumber = phoneNumberEdt.text.toString()
                val registerRequest = RegisterRequest(
                    full_name = nameEdt.text.toString().trim(),
                    email = emailEdt.text.toString().trim(),
                    password = passwordEdt.text.toString().trim(),
                    phone_number = phoneNumber,
                    address = addressEdt.text.toString().trim()
                )
                logd("Register fragment => $phoneNumber")
                viewModel.doRegisterRequest(registerRequest)
            }

        }
        observeUI()
    }
    private fun observeUI(){
        viewModel.register.observe(viewLifecycleOwner){
            when(it){
                is Result.Error ->{

                    it.error?.let { error ->
                        view?.showShortSnackbar(error)
                    }
                }
            Result.Loading->{
                binding.registerBtn.text = "loading"
            }
                is Result.Success ->{
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }


}