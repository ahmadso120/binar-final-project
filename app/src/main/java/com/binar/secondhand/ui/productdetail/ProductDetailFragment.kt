package com.binar.secondhand.ui.productdetail

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.WishlistRequest
import com.binar.secondhand.data.source.remote.response.BuyerProductDetailResponse
import com.binar.secondhand.databinding.FragmentProductDetailBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.getInitialsName
import com.binar.secondhand.utils.ui.hide
import com.binar.secondhand.utils.ui.loadPhotoUrl
import com.binar.secondhand.utils.ui.show
import com.binar.secondhand.utils.ui.showShortSnackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailFragment : BaseFragment(R.layout.fragment_product_detail) {
    override var bottomNavigationViewVisibility = View.GONE

    private val binding: FragmentProductDetailBinding by viewBinding()

    private val viewModel by viewModel<ProductDetailViewModel>()

    private val args: ProductDetailFragmentArgs by navArgs()

    private var productId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productId = args.id
        productId?.let {
            viewModel.setIdProduct(it)
        }

        val navBackStackEntry = navController.getBackStackEntry(R.id.productDetailFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains("resultKey")
            ) {
                val result = navBackStackEntry.savedStateHandle.get<Boolean>("resultKey")
                result?.let {
                    productId?.let {
                        viewModel.setIdProduct(it)
                    }
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_wishlist -> {
                    productId?.let { productId ->
                        viewModel.setIdProductForWishlist(productId)
                    }
                    true
                }
                R.id.action_share -> {
                    true
                }
                else -> false
            }
        }

        observeUi()

        binding.apply {
            toolbar.setNavigationOnClickListener {
                navController.navigateUp()
            }
        }
    }

    private fun observeUi() {
        viewModel.getProductIsWishlist.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    if (!it.data.isNullOrEmpty()) {
                        setIconWishlist(R.drawable.fi_heart_selected)
                    } else {
                        setIconWishlist(R.drawable.fi_heart)
                    }
                }
            }
        }
        viewModel.checkProductIsWishlist.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    if (!it.data.isNullOrEmpty()) {
                        val id: Int = it.data.map { wishlistResponse ->
                            wishlistResponse.id
                        }[0]
                        lifecycleScope.launch {
                            val result = viewModel.deleteWishlist(id)
                            if (result) {
                                setIconWishlist(R.drawable.fi_heart)
                                view?.showShortSnackbar("Berhasil menghapus produk dari wishlist")
                            } else {
                                view?.showShortSnackbar(
                                    "Gagal menghapus produk dari wishlist",
                                    false
                                )
                            }
                        }
                    } else {
                        lifecycleScope.launch {
                            productId?.let { productId ->
                                val result = viewModel.wishlist(
                                    WishlistRequest(productId)
                                )
                                if (result) {
                                    setIconWishlist(R.drawable.fi_heart_selected)
                                    view?.showShortSnackbar("Berhasil menambahkan produk ke wishlist")
                                } else {
                                    view?.showShortSnackbar(
                                        "Gagal menambahkan produk ke wishlist",
                                        false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        viewModel.getProductDetail.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    showErrorState()
                }
                Result.Loading -> {
                    showLoadingState()
                }
                is Result.Success -> {
                    if (it.data == null) {
                        view?.showShortSnackbar("Produk tidak ditemukan", false)
                        navController.navigateUp()
                    } else {
                        setupUi(it.data)
                    }
                }
            }
        }

        viewModel.hasProductOrdered.observe(viewLifecycleOwner) {
            showSuccessState()
            if (it) {
                binding.interestProductButton.apply {
                    isEnabled = false
                    text = "Menunggu respon penjual"
                }
            }
        }
    }

    private fun setupUi(data: BuyerProductDetailResponse) {
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
                initialsTv.text = data.user.fullName.getInitialsName()
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
                            val action = ProductDetailFragmentDirections
                                .actionProductDetailFragmentToInputBidPriceBottomSheet(data)
                            navController.navigate(action)
                        }
                    }
                }
            }
        }
    }

    private fun setIconWishlist(@DrawableRes icon: Int) {
        binding.toolbar.menu.findItem(R.id.action_wishlist).icon =
            ContextCompat.getDrawable(requireContext(), icon)
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