package com.binar.secondhand.ui.sellerproduct.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentSellerProductDetailBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.ui.loadPhotoUrl
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class SellerProductDetailFragment : BaseFragment(R.layout.fragment_seller_product_detail) {
    override var bottomNavigationViewVisibility = View.GONE
    val viewModel by viewModel<SellerProductDetailViewModel>()
    private val binding: FragmentSellerProductDetailBinding by viewBinding()
    private val args : SellerProductDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window = activity?.window
        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val toolbar: MaterialToolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        observeUI(args.id)
    }
    @SuppressLint("SetTextI18n")
    private fun observeUI(id:Int){
        viewModel.getProductDetail(id).observe(viewLifecycleOwner){product ->
            when(product){
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    binding.apply {
                        product.data.imageUrl?.let { expandedImage.loadPhotoUrl(it) }
                        productNameTextView.text= product.data.name
                        categoriesTextView.text = product.data.categories.joinToString {
                            it.name
                        }
                        priceTextView.text = "Rp. "+ product.data.basePrice.currencyFormatter()
                        productDescTextView.text = product.data.description
                    }
                }
            }
        }
        viewModel.getSellerName().observe(viewLifecycleOwner){ seller->
            when(seller){
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    binding.apply {
                        seller.data.apply {
                            if (imageUrl.isNullOrEmpty()){
                                sellerProfileImage.visibility = View.GONE
                                initialsTextView.visibility = View.VISIBLE
                                val name = fullName
                                val initials = name.trim()
                                    .splitToSequence(" ", limit = 2)
                                    .map { it.first() }
                                    .joinToString("").uppercase()
                                initialsTextView.text = initials
                            }else{
                                sellerProfileImage.loadPhotoUrl(imageUrl)
                            }
                            sellerNameTextView.text = fullName
                            cityTextView.text = city
                        }
                    }
                }
            }
        }
    }
}