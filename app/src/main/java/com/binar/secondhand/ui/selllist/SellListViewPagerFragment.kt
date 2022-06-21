package com.binar.secondhand.ui.selllist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.databinding.FragmentSellListViewPagerBinding

class SellListViewPagerFragment : BaseFragment(R.layout.fragment_sell_list_view_pager) {
    override var bottomNavigationViewVisibility = View.VISIBLE

    private val binding: FragmentSellListViewPagerBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}