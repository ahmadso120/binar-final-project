package com.binar.secondhand.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey val id:Int?,
    val historySearch : String?
)