package com.binar.secondhand.ui.selllist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.databinding.FragmentSellListBinding
import com.binar.secondhand.utils.ui.ConvertPixel
import com.binar.secondhand.utils.ui.setMargin
import com.google.android.material.tabs.TabLayoutMediator

class SellListFragment : BaseFragment(R.layout.fragment_sell_list) {
    override var bottomNavigationViewVisibility = View.VISIBLE

    private val binding: FragmentSellListBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
            tab.icon = ContextCompat.getDrawable(
                requireContext(), TAB_ICONS[position]
            )
        }.attach()

        binding.tabs.setMargin()

        (activity as AppCompatActivity).supportActionBar?.elevation = 0f
    }

    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.interested,
            R.string.sold
        )
        val TAB_ICONS = intArrayOf(
            R.drawable.ic_heart_tab_layout,
            R.drawable.ic_dollar_sign_tab_layout,
        )
    }
}