package com.binar.secondhand.data.source.remote.request

data class AccountSettingRequest(

    val current_password : String,
    val new_password : String,
    val confirm_password : String
) {
}