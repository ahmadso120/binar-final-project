package com.binar.secondhand.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.binar.secondhand.data.source.local.entity.SearchHistory
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history")
    fun getSearchHistory(): Flow<List<SearchHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: SearchHistory):Long

    @Query("DELETE FROM search_history")
    suspend fun deleteAllSearchHistory()

   @Delete
    suspend fun delete(historySearch : SearchHistory)
}