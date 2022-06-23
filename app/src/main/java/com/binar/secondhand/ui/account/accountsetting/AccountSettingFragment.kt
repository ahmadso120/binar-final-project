package com.binar.secondhand.ui.account.accountsetting

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.AccountSettingRequest
import com.binar.secondhand.databinding.FragmentAccountSettingBinding
import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.utils.LogoutProcess
import com.binar.secondhand.utils.ui.showShortSnackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountSettingFragment : BaseFragment(R.layout.fragment_account_setting) {
    private val binding: FragmentAccountSettingBinding by viewBinding()

    override var bottomNavigationViewVisibility = View.GONE

    private val viewModel by viewModel<AccountSettingViewModel>()
    private val appLocalData: AppLocalData by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changePassEmail()
        getResp()
    }




    private fun changePassEmail (){

        binding.apply {

            button.setOnClickListener{
                val newPassword = passwordEdt.text.toString()
                val confirmPassword = repeatPassEdt.text.toString()
                val accReq = AccountSettingRequest(
                    password = newPassword,
                    city = ""
                )
                if (newPassword=="" || confirmPassword==""){
//                    view?.showShortSnackbar("isi terlebih dahulu")
                    errorText.text = "isi terlebih dahulu"
                    errorText.visibility = View.VISIBLE
                }else if(newPassword != confirmPassword){
//                    view?.showShortSnackbar("password dan konfirmasi password berbeda")
                    errorText.text = "password dan konfirmasi password berbeda"
                    errorText.visibility = View.VISIBLE
                }else{
                    viewModel.getDataChange(accReq)
                    button.isEnabled=false
                    errorText.visibility = View.GONE
                }

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
                        val name = it.data.fullName
                        view?.showShortSnackbar("Halo ${name},passwordmu sudah diganti")
                    }
                }
            }
        }
    }
}