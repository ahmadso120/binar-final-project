package com.binar.secondhand.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BuyerProductDetailResponse(
    val Categories: List<CategoryResponse>?,
    @field:SerializedName("base_price")
    val basePrice: Int?,
    val createdAt: String,
    val description: String?,
    val id: Int,
    @field:SerializedName("image_name")
    val imageName: String?,
    @field:SerializedName("image_url")
    val imageUrl: String?,
    val location: String?,
    val name: String?,
    val status: String?,
    val updatedAt: String,
    @field:SerializedName("user_id")
    val userId: Int,
    @field:SerializedName("User")
    val user: UserResponse
) : Parcelable
