package com.binar.secondhand.ui.selllist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.binar.secondhand.ui.selllist.SellListFragment.Companion.TAB_TITLES

class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {

        val fragment = SellListViewPagerFragment()
        fragment.arguments = Bundle().apply {
            putInt("section_number", position)
        }
        return fragment
    }
}