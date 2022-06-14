package com.binar.secondhand.ui.home

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentHomeBinding
import com.binar.secondhand.utils.logd
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    override var bottomNavigationViewVisibility = View.VISIBLE

    private val binding: FragmentHomeBinding by viewBinding()

    private val viewModel by viewModel<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.buyerProductsMediatorData.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    logd(it.data.toString())
                }
            }
        }
    }
}