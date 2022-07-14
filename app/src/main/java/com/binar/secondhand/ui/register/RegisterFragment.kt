package com.binar.secondhand.ui.register

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.RegisterRequest
import com.binar.secondhand.databinding.FragmentRegisterBinding
import com.binar.secondhand.utils.ui.showShortSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment(R.layout.fragment_register) {
    private val binding: FragmentRegisterBinding by viewBinding()

    override var bottomNavigationViewVisibility = View.GONE

    private val viewModel by viewModel<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            registerBtn.setOnClickListener {
                register()
            }
        }
        observeUI()
        setupObserve()
    }
    private fun register(){
        val fullName = binding.nameEdt.text.toString()
        val email = binding.emailEdt.text.toString()
        val password = binding.passwordEdt.text.toString()
        val accountRequest = RegisterRequest(
            fullName,
            email,
            password

        )
            viewModel.doRegisterRequest(accountRequest)

        }
    private fun setupObserve(){
        binding.materialToolbar2.setNavigationOnClickListener {
            navController.navigateUp()
        }
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
                    navController.popBackStack()
                }
            }
        }
    }
}