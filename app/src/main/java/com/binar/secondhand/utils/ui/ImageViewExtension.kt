package com.binar.secondhand.utils.ui

import android.widget.ImageView
import com.binar.secondhand.GlideApp
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

const val CROSS_FADE_DURATION = 350

fun ImageView.loadPhotoUrl(
    url: String
) {
    GlideApp.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
        .into(this)
        .clearOnDetach()
}