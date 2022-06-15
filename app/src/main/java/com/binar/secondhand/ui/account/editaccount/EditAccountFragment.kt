package com.binar.secondhand.ui.account.editaccount


import android.os.Bundle
import android.util.Log
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentEditAccountBinding
import com.binar.secondhand.utils.loadPhotoUrl
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditAccountFragment : BaseFragment(R.layout.fragment_edit_account) {
    private val binding: FragmentEditAccountBinding by viewBinding()

    override var bottomNavigationViewVisibility = View.GONE

    private val viewModel by viewModel<EditAccountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUI()
    }

    private fun observeUI() {
        viewModel.getAccount().observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    binding.nameEdt.setText(it.data.fullName)
                    binding.addressEdt.setText(it.data.address)
                    binding.phoneNumberEdt.setText(it.data.phoneNumber)
                    if (it.data.imageUrl != null){
                       binding.profileImageView.loadPhotoUrl(it.data.imageUrl)
                    }
                }
            }
        }
    }
}