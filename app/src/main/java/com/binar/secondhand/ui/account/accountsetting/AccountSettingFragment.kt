package com.binar.secondhand.ui.account.accountsetting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.AccountReq
import com.binar.secondhand.databinding.FragmentAccountSettingBinding
import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.storage.UserLoggedIn
import com.binar.secondhand.utils.LogoutProcess
import com.binar.secondhand.utils.showShortSnackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountSettingFragment : BaseFragment(R.layout.fragment_account_setting) {
    private val binding: FragmentAccountSettingBinding by viewBinding()

    override var bottomNavigationViewVisibility = View.GONE

    private val viewModel by viewModel<AccountSettingViewModel>()
    private val appLocalData: AppLocalData by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showData()
        changePassEmail()
        getResp()
    }
    private fun showData(){
        viewModel.getUser().observe(viewLifecycleOwner){
            binding.apply {
                when(it){
                    is Result.Error ->{

                    }is Result.Loading -> {

                    }is Result.Success -> {
                        val email = it.data.email
                        emailEdt.setText(email)
                    }
                }
            }
        }
    }

    private fun changePassEmail (){

        binding.apply {
            button.setOnClickListener{
                val accReq = AccountReq(
                    email =emailEdt.text.toString(),
                    password = passwordEdt.text.toString(),
                    phone_number = null,
                    address = null,
                    image = null,
                    full_name = null
                )
                viewModel.getDataChange(accReq)

            }

        }

    }
    private fun getResp(){
        binding.apply {
            viewModel.userResp.observe(viewLifecycleOwner){
                when(it) {
                    is Result.Error -> {
                        it.error?.let { err ->
                            view?.showShortSnackbar(err)
                        }

                    }
                    Result.Loading -> {

                    }
                    is Result.Success -> {
                        view?.showShortSnackbar(it.data.email)
                        LogoutProcess.execute(appLocalData, binding)

                    }
                }
            }
        }
    }
}