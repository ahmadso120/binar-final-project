package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.ApiResponse
import com.binar.secondhand.data.source.remote.network.SearchService
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class SearchDataSource(
    private val searchService: SearchService
){
    suspend fun searchQuery(query:String) = searchService.getBuyerProductBySearch(query)
}