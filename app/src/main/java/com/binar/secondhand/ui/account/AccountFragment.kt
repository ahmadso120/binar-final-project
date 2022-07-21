package com.binar.secondhand.ui.account

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentAccountBinding
import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.ui.account.editaccount.EditAccountViewModel
import com.binar.secondhand.ui.search.SearchViewModel
import com.binar.secondhand.utils.LogoutProcess
import com.binar.secondhand.utils.getInitialsName
import com.binar.secondhand.utils.logd
import com.binar.secondhand.utils.ui.loadPhotoUrl
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : BaseFragment(R.layout.fragment_account) {
    override var requireAuthentication = true

    private val binding: FragmentAccountBinding by viewBinding()

    private val viewModel by viewModel<EditAccountViewModel>()

    private val appLocalData: AppLocalData by inject()
    private lateinit var builder: AlertDialog.Builder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUI()

        val listView = binding.listview
        val menuAccount: ArrayList<MenuAccount> = ArrayList()
        menuAccount.add(MenuAccount("Wishlist", R.drawable.ic_wishlist))
        menuAccount.add(MenuAccount("Daftar Order", R.drawable.ic_orders))
        menuAccount.add(MenuAccount("Riwayat", R.drawable.ic_baseline_history_edu_24))
        menuAccount.add(MenuAccount("Ubah Akun", R.drawable.ic_fi_edit))
        menuAccount.add(MenuAccount("Ubah Password", R.drawable.ic_fi_settings))
        menuAccount.add(MenuAccount("Keluar", R.drawable.ic_fi_log_out))
        listView.adapter = CustomAdapterAccount(requireContext(), menuAccount)
        listView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                3 -> {
                    navController.navigate(R.id.action_accountFragment_to_editAccountFragment)
                }
                4 -> {
                    navController.navigate(R.id.action_accountFragment_to_accountSettingFragment2)
                }
                5 -> {
                    builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Keluar dari SecondHand")
                        .setMessage("Apakah anda ingin keluar ?")
                        .setCancelable(true)
                        .setPositiveButton("Keluar") { _, _ ->
                            LogoutProcess.execute(
                                appLocalData,
                                navController
                            )
                        }
                        .setNegativeButton("Batal") { dialogInterface, _ ->
                            dialogInterface.cancel()
                        }
                        .show()
                }
                1 -> {
                    findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToBuyerOrderFragment())
                }

                2 -> {
                    findNavController().navigate(R.id.action_accountFragment_to_historyFragment)
                }
                0 -> {
                    findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToWishlistFragment())
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
                        if (it.data.imageUrl.isNullOrEmpty()) {
                            initialsNameTv.text = it.data.fullName.getInitialsName()
                        } else {
                            profileImageView.loadPhotoUrl(it.data.imageUrl)
                        }
                    }
                }
            }
        }
    }
}
