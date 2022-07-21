package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.BuyerOrderService
import com.binar.secondhand.data.source.remote.request.BidProductRequest
import com.binar.secondhand.data.source.remote.request.RebidBuyerOrderRequest

class BuyerOrderRemoteDataSource(
    private val service: BuyerOrderService
) {
    suspend fun bidProduct(bidProductRequest: BidProductRequest) = service.bidProduct(bidProductRequest)


    suspend fun getAllBuyerOrder() = service.getAllBuyerOrder()

    suspend fun deleteBuyerOrder(id: Int) = service.deleteBuyerOrder(id)

    suspend fun updateBidPrice(id: Int, rebidBuyerOrderRequest: RebidBuyerOrderRequest)= service.updateBidPrice(id,rebidBuyerOrderRequest)

    suspend fun hasProductOrdered() = service.hasProductOrdered()
}