package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.response.BuyerProductDataResponse
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BuyerProductService {
    @GET("buyer/product")
    suspend fun getBuyerProductByCategory(
        @Query("category_id") categoryId: Int? = null,
        @Query("status") status: String = "available"
    ): List<BuyerProductResponse>

    @GET("buyer/product")
    suspend fun getBuyerProduct(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("category_id") categoryId: Int? = null,
        @Query("status") status: String = "available"
    ): BuyerProductDataResponse


    @GET("buyer/product/{id}")
    suspend fun getBuyerProductById(
        @Path("id") id: Int
    ): Response<BuyerProductResponse>
}