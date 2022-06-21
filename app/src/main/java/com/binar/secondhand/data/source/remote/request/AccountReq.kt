package com.binar.secondhand.data.source.remote.request

data class AccountReq(
    val full_name : String?,
    val email : String,
    val password : String,
    val phone_number : Int?,
    val address : String?,
    val image : String?
) {
}