package com.binar.secondhand.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.widget.Toast
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.DateTimeException


inline val <reified T> T.TAG: String
    get() = T::class.java.simpleName

inline fun <reified T> T.logv(message: String) = Timber.tag(TAG).v(message)
inline fun <reified T> T.logi(message: String) = Timber.tag(TAG).i(message)
inline fun <reified T> T.logw(message: String) = Timber.tag(TAG).w(message)
inline fun <reified T> T.logd(message: String) = Timber.tag(TAG).d(message)
inline fun <reified T> T.loge(message: String) = Timber.tag(TAG).e(message)

fun Int.currencyFormatter(): String {
    return String.format("%,d", this).replace(',', '.')
}

fun String.dateTimeFormatter(): String? {
    try {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val output = SimpleDateFormat("dd MMM, HH:mm")
        val date = input.parse(this)
        if (date != null) {
            return output.format(date)
        }
        return null
    } catch (e: Exception) {
        loge("dateTimeFormatter => ${e.message}")
        return null
    }
}

fun String.getInitialsName(): String {
    return this.trim()
        .splitToSequence(" ", limit = 2)
        .map { it.first() }
        .joinToString("").uppercase()
}

fun String.openWhatsApp(context: Context) {
    if (this[0] == '0') {
        this.substring(1)
    }
    val contact = "+62$this"
    val url = "https://api.whatsapp.com/send?phone=$contact"
    try {
        val pm: PackageManager = context.packageManager
        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        context.startActivity(i)
    } catch (e: NameNotFoundException) {
        Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_LONG)
            .show()
        loge(e.printStackTrace().toString())
    }
}