package com.binar.secondhand.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buyer_product")
data class BuyerProductEntity(
    @ColumnInfo(name = "base_price")
    val basePrice: Int?,
    val createdAt: String,
    val description: String?,
    @PrimaryKey val buyerProductId: Int,
    @ColumnInfo(name = "image_name")
    val imageName: String?,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    val location: String?,
    val name: String?,
    val status: String?,
    val updatedAt: String,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    val isFavorite: Boolean = false
)
