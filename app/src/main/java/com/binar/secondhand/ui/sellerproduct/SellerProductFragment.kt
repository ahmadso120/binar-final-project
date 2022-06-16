package com.binar.secondhand.ui.sellerproduct

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.databinding.FragmentSellerProductBinding

class SellerProductFragment : BaseFragment(R.layout.fragment_seller_product) {

    override var bottomNavigationViewVisibility = View.VISIBLE

    private val binding: FragmentSellerProductBinding by viewBinding()
}