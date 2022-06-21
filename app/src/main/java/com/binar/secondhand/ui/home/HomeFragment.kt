package com.binar.secondhand.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible

import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentHomeBinding
import com.binar.secondhand.ui.common.ProductAdapter
import com.binar.secondhand.utils.EventObserver

import com.binar.secondhand.utils.logd
import com.binar.secondhand.utils.showShortSnackbar
import com.google.android.material.appbar.MaterialToolbar

import com.binar.secondhand.utils.RECYCLER_VIEW_CACHE_SIZE
import com.binar.secondhand.utils.setupLayoutManager

import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalBadgeUtils
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    override var bottomNavigationViewVisibility = View.VISIBLE

    private val binding: FragmentHomeBinding by viewBinding()

    private val viewModel by sharedViewModel<HomeViewModel>()

    private lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //sementara
        val toolbar: MaterialToolbar = binding.toolbar
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.notification ->{
                    findNavController().navigate(R.id.action_homeFragment_to_notificationFragment2)
                    true
                }
                else -> false
            }
        }

        binding.filterButton.setOnClickListener { showFilterBottomSheet() }


        setBadgeCountNotification(3)

        setupAdapter()

        observeUi(view)
    }

    private fun setBadgeCountNotification(count: Int) {
        val badgeDrawable = BadgeDrawable.create(requireContext())
        badgeDrawable.number = count
        BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.toolbar, R.id.notification)
    }

    private fun observeUi(view: View) {
        viewModel.buyerProductsLiveData.observe(viewLifecycleOwner) { it ->
            when(it) {
                is Result.Error -> {
                    showErrorState()
//                    view.showShortSnackbar(it.error.toString())
                }
                Result.Loading -> { showLoadingState() }
                is Result.Success -> {
                    showSuccessState()
                    productAdapter.submitList(it.data)
                }
            }
        }

        viewModel.navigateToBuyerProductDetail.observe(viewLifecycleOwner, EventObserver {
//            findNavController().navigate()
        })
    }

    private fun setupAdapter() {
        val itemSpacing = resources.getDimensionPixelSize(R.dimen.margin_normal)

        productAdapter = ProductAdapter {
            viewModel.onBuyerProductClicked(it)
        }
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            }
            adapter = productAdapter
            setupLayoutManager(
                spacing = itemSpacing
            )
            setItemViewCacheSize(RECYCLER_VIEW_CACHE_SIZE)
        }
    }

    private fun showFilterBottomSheet() {
        HomeProductFilterBottomSheet
            .newInstance()
            .show(childFragmentManager, HomeProductFilterBottomSheet.TAG)
    }

    private fun showSuccessState() {
        binding.recyclerView.isVisible = true
        binding.contentLoadingLayout.hide()
    }

    private fun showErrorState() {
        binding.recyclerView.isVisible = true
        binding.contentLoadingLayout.hide()
    }

    private fun showLoadingState() {
        binding.recyclerView.isVisible = false
        binding.contentLoadingLayout.show()
    }
}