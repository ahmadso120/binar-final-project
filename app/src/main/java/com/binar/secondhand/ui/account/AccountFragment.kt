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


class AccountFragment : BaseFragment(R.layout.fragment_account) {
    override var bottomNavigationViewVisibility = View.VISIBLE

    private val binding: FragmentAccountBinding by viewBinding()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_editAccountFragment)
        }
    }
}