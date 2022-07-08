package com.binar.secondhand.data

import com.binar.secondhand.data.source.remote.BuyerOrderRemoteDataSource
import com.binar.secondhand.data.source.remote.request.BidProductRequest
import com.binar.secondhand.utils.loge

interface BuyerOrderRepository {
    suspend fun bidProduct(bidProductRequest: BidProductRequest) : Boolean
}

class BuyerOrderRepositoryImpl(
    private val dataSource: BuyerOrderRemoteDataSource
) : BuyerOrderRepository {
    override suspend fun bidProduct(bidProductRequest: BidProductRequest): Boolean {
        return try {
            val response = dataSource.bidProduct(bidProductRequest)
            response.isSuccessful
        } catch (e: Exception) {
            loge("updateStatusOrder() => ${e.message}")
            false
        }
    }

}