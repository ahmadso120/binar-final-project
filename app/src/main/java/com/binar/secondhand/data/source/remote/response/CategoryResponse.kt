package com.binar.secondhand.data.source.remote.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryResponse(
    val id: Int,
    val name: String
) : Parcelable