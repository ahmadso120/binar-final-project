package com.binar.secondhand.ui.sellerorder

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.binar.secondhand.ui.sellerorder.SellerOrderFragment.Companion.TAB_TITLES

class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {

        val fragment = SellerOrderViewPagerFragment()
        fragment.arguments = Bundle().apply {
            putInt(SECTION_NUMBER, position)
        }
        return fragment
    }

    companion object {
        const val SECTION_NUMBER = "SECTION_NUMBER"
    }
}