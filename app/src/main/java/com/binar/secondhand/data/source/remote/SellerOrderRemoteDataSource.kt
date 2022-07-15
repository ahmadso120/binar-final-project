package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.SellerOrderService
import okhttp3.RequestBody

class SellerOrderRemoteDataSource(
    private val service: SellerOrderService
) {
    suspend fun getSellerOrder() = service.getSellerOrder()

    suspend fun updateStatusOrder(id: Int, status: RequestBody) = service.updateStatusOrder(id, status)

    suspend fun getSellerOrderById(id: Int) = service.getSellerOrderById(id)
}