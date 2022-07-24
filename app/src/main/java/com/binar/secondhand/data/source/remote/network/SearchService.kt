package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("buyer/product")
    suspend fun getBuyerProductBySearch(
        @Query("search") search: String,
        @Query("status") status: String
    ): Response<List<BuyerProductResponse>>

}