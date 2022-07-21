package com.binar.secondhand.ui.sellerorder

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentSellerOrderViewPagerBinding
import com.binar.secondhand.ui.common.SellerOrderAdapter
import com.binar.secondhand.ui.sellerorder.SectionsPagerAdapter.Companion.SECTION_NUMBER
import com.binar.secondhand.utils.EventObserver
import com.binar.secondhand.utils.logd
import com.binar.secondhand.utils.ui.hide
import com.binar.secondhand.utils.ui.show
import com.binar.secondhand.utils.ui.showShortSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerOrderViewPagerFragment : BaseFragment(R.layout.fragment_seller_order_view_pager) {
    private val binding: FragmentSellerOrderViewPagerBinding by viewBinding()

    private lateinit var sellerOrderAdapter: SellerOrderAdapter

    private val viewModel by viewModel<SellerOrderViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeUi()
    }

    private fun observeUi() {
        val isInterested = arguments?.getInt(SECTION_NUMBER)
        if (isInterested == 0) {
            viewModel.interestedOrder.observe(viewLifecycleOwner) {
                when(it) {
                    is Result.Error -> {
                        logd("error")
                        showErrorState(it.error)
                    }
                    Result.Loading -> {
                        logd("loading")
                        showLoadingState()
                    }
                    is Result.Success -> {
                        logd("data => ${it.data}")
                        if (it.data.isNotEmpty()) {
                            showSuccessState()
                            sellerOrderAdapter.submitList(it.data)
                        } else {
                            showEmptyState(getString(R.string.empty_state_order_text))
                        }
                    }
                }
            }
        } else {
            viewModel.soldOrder.observe(viewLifecycleOwner) {
                when(it) {
                    is Result.Error -> {
                        showErrorState(it.error)
                    }
                    Result.Loading -> {
                        showLoadingState()
                    }
                    is Result.Success -> {
                        if (it.data.isNotEmpty()) {
                            showSuccessState()
                            sellerOrderAdapter.submitList(it.data)
                        } else {
                            showEmptyState("Belum ada produkmu yang terjual")
                        }
                    }
                }
            }
        }
        viewModel.navigateToBidderInfo.observe(viewLifecycleOwner, EventObserver {
            val action = SellerOrderFragmentDirections.actionSellListFragmentToBidderInfoFragment(it.id)
            findNavController().navigate(action)
        })
    }

    private fun setupAdapter() {
        sellerOrderAdapter = SellerOrderAdapter {
            viewModel.onOrderClicked(it)
        }
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = sellerOrderAdapter
        }
    }

    private fun showSuccessState() {
        binding.contentLoadingLayout.hide()
        binding.emptyStateLayout.root.hide()
        binding.recyclerview.show()
    }

    private fun showErrorState(error: String?) {
        binding.contentLoadingLayout.hide()
        binding.emptyStateLayout.root.hide()
        binding.recyclerview.hide()
        error?.let {
            view?.showShortSnackbar(it)
        }
    }

    private fun showEmptyState(emptyText: String) {
        binding.contentLoadingLayout.hide()
        binding.recyclerview.hide()
        binding.emptyStateLayout.emptyText.text = emptyText
        binding.emptyStateLayout.root.show()
    }

    private fun showLoadingState() {
        binding.contentLoadingLayout.show()
        binding.recyclerview.hide()
    }
}