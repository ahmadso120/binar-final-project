package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.BuyerOrderService
import com.binar.secondhand.data.source.remote.request.BidProductRequest

class BuyerOrderRemoteDataSource(
    private val service: BuyerOrderService
) {
    suspend fun bidProduct(bidProductRequest: BidProductRequest) = service.bidProduct(bidProductRequest)

    suspend fun hasProductOrdered() = service.hasProductOrdered()
}