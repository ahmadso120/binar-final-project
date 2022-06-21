package com.binar.secondhand.ui.account

import android.os.Bundle
import android.view.View

import android.widget.Adapter
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController


import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment

import com.binar.secondhand.data.Result

import com.binar.secondhand.databinding.FragmentAccountBinding
import androidx.navigation.fragment.findNavController

import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.ui.account.editaccount.EditAccountViewModel
import com.binar.secondhand.utils.LogoutProcess
import kotlinx.coroutines.NonDisposableHandle.parent
import com.binar.secondhand.utils.ui.loadPhotoUrl
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class AccountFragment : BaseFragment(R.layout.fragment_account) {
    private var getFile: File? = null
    private val binding: FragmentAccountBinding by viewBinding()
    override var bottomNavigationViewVisibility = View.VISIBLE
    private val viewModel by viewModel<EditAccountViewModel>()
    private val appLocalData: AppLocalData by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUI()

        var listView = binding.listview
        var menuAccount: ArrayList<MenuAccount> = ArrayList()
        menuAccount.add(MenuAccount("Ubah Akun", R.drawable.ic_fi_edit))
        menuAccount.add(MenuAccount("Ubah Password", R.drawable.ic_fi_settings))
        menuAccount.add(MenuAccount("Keluar", R.drawable.ic_fi_log_out))

        listView.adapter = CustomAdapterAccount(requireContext(), menuAccount)
        listView.setOnItemClickListener { AdapterView, View, position, id ->
            when (position) {
                0 -> {
                    findNavController().navigate(R.id.action_accountFragment_to_editAccountFragment)
                }
                1 -> {
                         findNavController().navigate(R.id.action_accountFragment_to_accountSettingFragment2)
                }
                2 -> {
                    LogoutProcess.execute(appLocalData, binding)
                }

        }
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
                        if (getFile == null) {
                            with(profileImageView) { it.data.imageUrl?.let { it1 -> loadPhotoUrl(it1) } }
                        }
                    } else {
                        binding.profileImageView.setImageResource(R.drawable.ic_avatar)

                    }
                }

            }
        }
    }


}

}
