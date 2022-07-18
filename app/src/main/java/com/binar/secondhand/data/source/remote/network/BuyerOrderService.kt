package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.request.BidProductRequest
import com.binar.secondhand.data.source.remote.request.RebidBuyerOrderRequest
import com.binar.secondhand.data.source.remote.response.BuyerOrderResponse
import com.binar.secondhand.data.source.remote.response.DeleteResponse
import retrofit2.Response
import retrofit2.http.*
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
    suspend fun getAllBuyerOrder(): Response<List<BuyerOrderResponse>>

    @DELETE("buyer/order/{id}")
    suspend fun deleteBuyerOrder(
        @Path("id") id : Int
    ): Response<DeleteResponse>

    @PUT("buyer/order/{id}")
    suspend fun updateBidPrice(
        @Path("id") id: Int,
        @Body rebidBuyerOrderRequest: RebidBuyerOrderRequest
    ): Response<BuyerOrderResponse>
    suspend fun hasProductOrdered() : Response<List<HasProductOrdered>>
}