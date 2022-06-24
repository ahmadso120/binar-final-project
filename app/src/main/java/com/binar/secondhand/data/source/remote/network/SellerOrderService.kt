package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.response.SellerOrderResponse
import retrofit2.Response
import retrofit2.http.GET

interface SellerOrderService {
    @GET("seller/order")
    suspend fun getSellerOrder(): Response<List<SellerOrderResponse>>
}