package com.binar.secondhand.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment

import com.binar.secondhand.data.Result

import com.binar.secondhand.databinding.FragmentAccountBinding
import androidx.navigation.fragment.findNavController

import com.binar.secondhand.databinding.FragmentHomeBinding
import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.ui.account.editaccount.EditAccountViewModel
import com.binar.secondhand.utils.LogoutProcess
import com.binar.secondhand.utils.loadPhotoUrl
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class AccountFragment : BaseFragment(R.layout.fragment_account) {
    private var getFile: File? = null
    private val binding: FragmentAccountBinding by viewBinding()
    override var bottomNavigationViewVisibility = View.GONE


    private val viewModel by viewModel<EditAccountViewModel>()
    private val appLocalData: AppLocalData by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUI()

        binding.icEdit.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_editAccountFragment)

        }
        binding.icSettings.setOnClickListener {

            findNavController().navigate(R.id.action_accountFragment_to_accountSettingFragment2)

        }

    }


    private fun observeUI() {
        viewModel.getAccount().observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {

                    binding.apply {

                        if (it.data.imageUrl != null) {
                            if (getFile == null){
                                with(profileImageView) { it.data.imageUrl?.let { it1 -> loadPhotoUrl(it1) } }
                            }
                        } else {
                            binding.profileImageView.setImageResource(R.drawable.ic_avatar)

                        }
                    }

                }
            }
        }
        binding.icLogout.setOnClickListener {
            LogoutProcess.execute(appLocalData, binding)
        }




    }

}


