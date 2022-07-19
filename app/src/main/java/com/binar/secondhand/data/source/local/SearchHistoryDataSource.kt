package com.binar.secondhand.data.source.local

import androidx.lifecycle.LiveData
import com.binar.secondhand.data.source.local.entity.SearchHistory
import com.binar.secondhand.data.source.local.room.SearchHistoryDao
import com.binar.secondhand.data.source.remote.network.SearchService

class SearchHistoryDataSource(
    private val searchService : SearchHistoryDao
){
    suspend fun getSearchHistory() = searchService.getSearchHystory()

    suspend fun insert(searchHistory: SearchHistory) = searchService.insert(searchHistory)
}