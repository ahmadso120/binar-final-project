package com.binar.secondhand.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.databinding.FragmentAccountBinding
import com.binar.secondhand.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountFragment : BaseFragment(R.layout.fragment_account) {

  /*  override var btmNavigationViewVisibilty = View.VISIBLE*/
    private val binding: FragmentAccountBinding by viewBinding()

    private val viewModel by viewModel<AccountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icEdit.setOnClickListener {
            /*findNavController().navigate(R.id.)
*/
        }
        binding.icSettings.setOnClickListener {

        }
        binding.icLogout.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
        }
    }



}