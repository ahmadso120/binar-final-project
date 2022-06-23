package com.binar.secondhand.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentHomeBinding
import com.binar.secondhand.ui.common.ProductAdapter
import com.binar.secondhand.utils.EventObserver
import com.binar.secondhand.utils.ui.RECYCLER_VIEW_CACHE_SIZE
import com.binar.secondhand.utils.ui.setupLayoutManager
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

    private var isRefreshing: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.notification ->{
                    findNavController().navigate(R.id.action_homeFragment_to_notificationFragment2)
                    true
                }
                else -> false
            }
        }

        binding.filterButton.setOnClickListener { showFilterBottomSheet() }

        binding.swipeRefreshLayout.setOnRefreshListener {
            isRefreshing = true
            viewModel.filterCategoryProduct(DEFAULT_CATEGORY_ID)
            viewModel.categoryId = DEFAULT_CATEGORY_ID
        }

        setBadgeCountNotification(3)

        setupAdapter()

        observeUi()
    }

    private fun setBadgeCountNotification(count: Int) {
        val badgeDrawable = BadgeDrawable.create(requireContext())
        badgeDrawable.number = count
        BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.toolbar, R.id.notification)
    }

    private fun observeUi() {
        viewModel.buyerProducts.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Error -> {
                    showErrorState()
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
        val itemSpacing = resources.getDimensionPixelSize(R.dimen.margin_padding_size_medium)

        productAdapter = ProductAdapter {
            viewModel.onBuyerProductClicked(it)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
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
        if (isRefreshing) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.contentLoadingLayout.hide()
        binding.recyclerView.isVisible = true
    }

    private fun showErrorState() {
        binding.contentLoadingLayout.hide()
        binding.recyclerView.isVisible = true
    }

    private fun showLoadingState() {
        if (isRefreshing) {
            binding.swipeRefreshLayout.isRefreshing = true
        }
        binding.contentLoadingLayout.show()
        binding.recyclerView.isVisible = false
    }

    companion object {
        const val DEFAULT_CATEGORY_ID = 0
    }
}