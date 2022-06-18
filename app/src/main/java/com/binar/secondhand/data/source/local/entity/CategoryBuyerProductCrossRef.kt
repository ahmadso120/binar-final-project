package com.binar.secondhand.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["bId", "cId"])
data class CategoryBuyerProductCrossRef (
    val bId: Int,
    @ColumnInfo(index = true)
    val cId: Int
)