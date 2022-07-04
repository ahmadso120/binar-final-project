package com.binar.secondhand.data.source.remote.request

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap

//data class RegisterRequest(
//    val full_name: String,
//    val email: String,
//    val password: String,
//    val phone_number: String,
//    val address :String,
//    val city    : String
//)

data class RegisterRequest(
@Part val file : MultipartBody.Part?,
@PartMap val partMap : Map<String,RequestBody>
)
