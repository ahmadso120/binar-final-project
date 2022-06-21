package com.binar.secondhand.utils.ui

import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

fun TabLayout.setMargin() {
    for (i in 0 until this.tabCount) {
        val tab = (this.getChildAt(0) as ViewGroup).getChildAt(i)
        val p = tab.layoutParams as ViewGroup.MarginLayoutParams
        if (i == 0) {
            p.setMargins(0, 0, ConvertPixel.dpToPx(8), 0)
        } else {
            p.setMargins(ConvertPixel.dpToPx(8), 0, 0, 0)
        }
        tab.requestLayout()
    }
}