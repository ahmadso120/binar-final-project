package com.binar.secondhand.ui.account.accountsetting

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.AccountReq
import com.binar.secondhand.databinding.FragmentAccountSettingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountSettingFragment : BaseFragment(R.layout.fragment_account_setting) {
    private val binding: FragmentAccountSettingBinding by viewBinding()

    override var bottomNavigationViewVisibility = View.GONE

    private val viewModel by viewModel<AccountSettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showData()
        changePassEmail()
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