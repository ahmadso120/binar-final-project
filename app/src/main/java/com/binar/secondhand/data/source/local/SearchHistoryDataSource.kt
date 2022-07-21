package com.binar.secondhand.data.source.local


import com.binar.secondhand.data.source.local.entity.SearchHistory
import com.binar.secondhand.data.source.local.room.SearchHistoryDao


class SearchHistoryDataSource(
    private val searchService : SearchHistoryDao
){
    fun getSearchHistory()  = searchService.getSearchHistory()

    suspend fun insert(searchHistory: SearchHistory) = searchService.insert(searchHistory)

    suspend fun deleteHistory() = searchService.deleteAllSearchHistory()

    suspend fun deleteHistoryId(historySearch : SearchHistory) = searchService.delete(historySearch)
}