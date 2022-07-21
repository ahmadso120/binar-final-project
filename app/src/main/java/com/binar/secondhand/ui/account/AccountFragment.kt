package com.binar.secondhand.ui.account

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentAccountBinding
import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.ui.account.editaccount.EditAccountViewModel
import com.binar.secondhand.ui.common.AuthViewModel
import com.binar.secondhand.ui.login.LoginFragment.Companion.LOGIN_SUCCESSFUL
import com.binar.secondhand.ui.search.SearchViewModel
import com.binar.secondhand.utils.LogoutProcess
import com.binar.secondhand.utils.navigateToStartDestination
import com.binar.secondhand.utils.ui.loadPhotoUrl
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : BaseFragment(R.layout.fragment_account) {
    override var bottomNavigationViewVisibility = View.VISIBLE

    private val binding: FragmentAccountBinding by viewBinding()

    private val viewModel by viewModel<EditAccountViewModel>()
    private val searchViewModel by viewModel<SearchViewModel>()
    private val authViewModel by viewModel<AuthViewModel>()

    private val appLocalData: AppLocalData by inject()
    private lateinit var builder: AlertDialog.Builder


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val savedStateHandle = navController.currentBackStackEntry!!.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(LOGIN_SUCCESSFUL).observe(viewLifecycleOwner) {
            if (!it) {
                navController.navigateToStartDestination()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            authViewModel.isUserHasLoggedIn.collect {
                if (!it) {
                    navController.navigate(R.id.loginFragment)
                }
            }
        }

        observeUI()
        var listView = binding.listview
        var menuAccount: ArrayList<MenuAccount> = ArrayList()
        menuAccount.add(MenuAccount("Ubah Akun", R.drawable.ic_fi_edit))
        menuAccount.add(MenuAccount("Ubah Password", R.drawable.ic_fi_settings))
        menuAccount.add(MenuAccount("Keluar", R.drawable.ic_fi_log_out))

        listView.adapter = CustomAdapterAccount(requireContext(), menuAccount)
        listView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    navController.navigate(R.id.action_accountFragment_to_editAccountFragment)
                }
                1 -> {
                    navController.navigate(R.id.action_accountFragment_to_accountSettingFragment2)
                }
                2 -> {
                    searchViewModel.deleteHistory()
                    //delete search history
                    builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Keluar dari SecondHand")
                        .setMessage("Apakah anda ingin keluar ?")
                        .setCancelable(true) // dialog box in cancellable
                        // set positive button
                        //take two parameters dialogInterface and an int
                        .setPositiveButton("Keluar") { dialogInterface, it ->
                            LogoutProcess.execute(
                                appLocalData,
                                binding.root.findNavController()
                            )  // close the app when yes clicked
                        }
                        .setNegativeButton("Batal") { dialogInterface, it ->
                            // cancel the dialogbox
                            dialogInterface.cancel()
                        }
                        .show()
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
                            profileImageView.loadPhotoUrl(it.data.imageUrl)
                        } else {
                            profileImageView.setImageResource(R.drawable.ic_avatar)
                        }
                    }
                }
            }
        }
    }
}
