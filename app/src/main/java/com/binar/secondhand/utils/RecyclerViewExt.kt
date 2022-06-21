package com.binar.secondhand.utils

import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.ui.widget.recyclerview.StaggeredGridItemOffsetDecoration

const val RECYCLER_VIEW_CACHE_SIZE = 3

fun RecyclerView.setupLayoutManager(
    spacing: Int
) {
    addItemDecoration(
        StaggeredGridItemOffsetDecoration(
            spacing
        )
    )
}