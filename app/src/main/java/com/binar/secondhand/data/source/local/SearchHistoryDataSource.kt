package com.binar.secondhand.data.source.local

import com.binar.secondhand.data.source.local.entity.SearchHistory
import com.binar.secondhand.data.source.local.room.SearchHistoryDao


class SearchHistoryDataSource(
    private val searchService : SearchHistoryDao
){
    suspend fun getSearchHistory() = searchService.getSearchHystory()

    suspend fun insert(searchHistory: SearchHistory) = searchService.insert(searchHistory)

    suspend fun deleteHistory() = searchService.deleteAllSearchHistory()
}