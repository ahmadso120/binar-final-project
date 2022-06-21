package com.binar.secondhand.utils.ui

import android.content.Context
import android.content.res.Resources

class ConvertPixel {
    companion object {
        fun dpToPx(dp: Int): Int {
            return ((dp * Resources.getSystem().displayMetrics.density).toInt());
        }
    }
}