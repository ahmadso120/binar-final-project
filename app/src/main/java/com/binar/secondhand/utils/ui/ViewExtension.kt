package com.binar.secondhand.utils.ui

import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.WindowInsetsCompat.Type
import com.google.android.material.snackbar.Snackbar

fun View.showShortSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.focusAndShowKeyboard() {
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                val controller = this.windowInsetsController
                controller?.show(Type.ime())
            }
        }
    }
    requestFocus()
    if (hasWindowFocus()) {
        showTheKeyboardNow()
    } else {
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}

fun View.hideKeyboard() {
    val controller = this.windowInsetsController
    controller?.show(Type.ime())
}