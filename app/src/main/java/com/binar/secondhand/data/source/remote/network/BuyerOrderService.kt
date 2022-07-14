package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.request.BidProductRequest
import com.binar.secondhand.data.source.remote.response.HasProductOrdered
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BuyerOrderService {

    @POST("buyer/order")
    suspend fun bidProduct(
        @Body bidProductRequest: BidProductRequest
    ) : Response<Unit>

    @GET("buyer/order")
    suspend fun hasProductOrdered() : Response<List<HasProductOrdered>>
}