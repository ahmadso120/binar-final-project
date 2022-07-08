package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.response.SellerOrderResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SellerOrderService {
    @GET("seller/order")
    suspend fun getSellerOrder(): Response<List<SellerOrderResponse>>

    @GET("seller/order/{id}")
    suspend fun getSellerOrderById(@Path("id") id: Int): Response<SellerOrderResponse>

    @PATCH("seller/order/{id}")
    suspend fun updateStatusOrder(
        @Path("id") id: Int,
        @Body status: RequestBody
    ): Response<Unit>
}