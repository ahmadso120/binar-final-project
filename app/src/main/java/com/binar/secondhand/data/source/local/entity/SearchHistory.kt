package com.binar.secondhand.data.source.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey val historySearch : String
):Parcelable