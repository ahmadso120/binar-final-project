package com.binar.secondhand.utils

import timber.log.Timber

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