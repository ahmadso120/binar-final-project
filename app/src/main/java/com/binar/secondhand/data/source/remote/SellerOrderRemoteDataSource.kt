package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.SellerOrderService

class SellerOrderRemoteDataSource(
    private val service: SellerOrderService
) {
    suspend fun getSellerOrder() = service.getSellerOrder()
}