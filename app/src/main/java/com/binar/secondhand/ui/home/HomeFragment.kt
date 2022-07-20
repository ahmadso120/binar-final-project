package com.binar.secondhand.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentHomeBinding
import com.binar.secondhand.ui.common.ProductAdapter
import com.binar.secondhand.ui.notification.NotificationViewModel
import com.binar.secondhand.utils.EventObserver
import com.binar.secondhand.utils.logd
import com.binar.secondhand.utils.ui.RECYCLER_VIEW_CACHE_SIZE
import com.binar.secondhand.utils.ui.setupLayoutManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalBadgeUtils
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private val binding: FragmentHomeBinding by viewBinding()

    private val viewModel by sharedViewModel<HomeViewModel>()
    private val notificationViewModel by viewModel<NotificationViewModel>()

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
                R.id.search ->{
                    findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
                    true
                }
                else -> false
            }
        }

        binding.filterButton.setOnClickListener { showFilterBottomSheet() }


//        binding.swipeRefreshLayout.setOnRefreshListener {
//            isRefreshing = true
//            viewModel.filterCategoryProduct(viewModel.categoryId)
//        }

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
           productAdapter.submitData(lifecycle, it)
        }
        notificationViewModel.getUnreadCount().observe(viewLifecycleOwner){
            when(it){
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> if (it.data != 0){
                    setBadgeCountNotification(it.data)
                }
            }
        }
        viewModel.navigateToBuyerProductDetail.observe(viewLifecycleOwner, EventObserver {
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(it.buyerProductId)
            findNavController().navigate(action)
        })
    }

    private fun setupAdapter() {
        val itemSpacing = resources.getDimensionPixelSize(R.dimen.margin_padding_size_medium)

        productAdapter = ProductAdapter {
            viewModel.onBuyerProductClicked(it)
        }

        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    productAdapter.retry()
                }
            )
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

    companion object {
        const val DEFAULT_CATEGORY_ID = 0
    }
}