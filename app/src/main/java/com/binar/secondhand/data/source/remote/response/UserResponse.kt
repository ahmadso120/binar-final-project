package com.binar.secondhand.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    val id: Int,
    val address: String?,
    val city: String?,
    val email: String,
    @field:SerializedName("full_name")
    val fullName: String?,
    @field:SerializedName("phone_number")
    val phoneNumber: String?,
    @field:SerializedName("image_url")
    val imageUrl: String?
) : Parcelable