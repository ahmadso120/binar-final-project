package com.binar.secondhand.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.binar.secondhand.data.source.local.entity.SearchHistory

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history")
    fun getSearchHystory():List<SearchHistory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: String)
}