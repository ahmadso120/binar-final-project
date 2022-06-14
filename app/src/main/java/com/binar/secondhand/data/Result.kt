package com.binar.secondhand.data

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val code: Int? = null, val error: String? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}