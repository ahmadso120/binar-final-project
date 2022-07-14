package com.binar.secondhand.ui.productdetail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.databinding.FragmentProductDetailBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.ui.hide
import com.binar.secondhand.utils.ui.loadPhotoUrl
import com.binar.secondhand.utils.ui.show
import com.binar.secondhand.utils.ui.showShortSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailFragment: BaseFragment(R.layout.fragment_product_detail) {
    override var bottomNavigationViewVisibility = View.GONE

    private val binding: FragmentProductDetailBinding by viewBinding()

    private val viewModel by viewModel<ProductDetailViewModel>()

    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.id
        if (id == 0) {
            navController.popBackStack()
        }

        viewModel.setIdProduct(id)

        observeUi()

        binding.apply {
            toolbar.setNavigationOnClickListener { view ->
                navController.navigateUp()
            }
        }
    }

    private fun observeUi() {
        viewModel.getProductDetail.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Error -> {
                    showErrorState()
                }
                Result.Loading -> {
                    showLoadingState()
                }
                is Result.Success -> {
                    if (it.data == null) {
                        view?.showShortSnackbar("Produk tidak ditemukan")
                        navController.popBackStack()
                    } else {
                        showSuccessState()
                        setupUi(it.data)
                    }
                }
            }
        }
    }

    private fun setupUi(data: BuyerProductResponse) {
        binding.apply {
            data.imageUrl?.let {
                detailImage.loadPhotoUrl(it)
            }
            productNameTv.text = data.name
            categoriesTv.text = data.Categories?.joinToString(separator = ", ") {
                it.name
            }
            basePriceTv.text = "Rp ${data.basePrice?.currencyFormatter()}"
            if (data.user.imageUrl.isNullOrEmpty()) {
                initialsTv.text = "AS"
            } else {
                userImage.loadPhotoUrl(data.user.imageUrl)
            }
            userNameTv.text = data.user.fullName
            cityTv.text = data.user.city
            descriptionTv.text = data.description

            interestProductButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                    authViewModel.isUserHasLoggedIn.collect {
                        if (!it) {
                            executeRequireAuthentication()
                        } else {
                            InputBidPriceBottomSheet
                                .newInstance(data)
                                .show(childFragmentManager, InputBidPriceBottomSheet.TAG)
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessState() {
        binding.contentLoadingLayout.hide()
        binding.productDetailScrollview.show()
        binding.interestProductButton.show()
    }

    private fun showErrorState() {
        binding.contentLoadingLayout.hide()
        binding.productDetailScrollview.hide()
        binding.interestProductButton.hide()
    }

    private fun showLoadingState() {
        binding.contentLoadingLayout.show()
        binding.productDetailScrollview.hide()
        binding.interestProductButton.hide()
    }
}