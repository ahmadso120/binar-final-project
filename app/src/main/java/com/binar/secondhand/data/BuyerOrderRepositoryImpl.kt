package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.BuyerOrderRemoteDataSource
import com.binar.secondhand.data.source.remote.request.BidProductRequest
import com.binar.secondhand.data.source.remote.response.HasProductOrdered
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers

interface BuyerOrderRepository {
    suspend fun bidProduct(bidProductRequest: BidProductRequest) : Boolean
    fun hasProductOrdered(id: Int): LiveData<Boolean>
}

class BuyerOrderRepositoryImpl(
    private val dataSource: BuyerOrderRemoteDataSource
) : BuyerOrderRepository {
    override suspend fun bidProduct(bidProductRequest: BidProductRequest): Boolean {
        return try {
            val response = dataSource.bidProduct(bidProductRequest)
            response.isSuccessful
        } catch (e: Exception) {
            loge("bidProduct() => ${e.message}")
            false
        }
    }

    override fun hasProductOrdered(id: Int): LiveData<Boolean> = liveData(Dispatchers.IO){
        try {
            val response = dataSource.hasProductOrdered()
            if (response.isSuccessful) {
                val data = response.body()
                data?.let {
                    emit(it.any { hasProductOrdered ->  hasProductOrdered.productId == id })
                } ?: run {
                    emit(false)
                }
            } else {
                emit(false)
            }
        } catch (e: Exception) {
            loge("hasProductOrdered() => ${e.message}")
            emit(false)
        }
    }
}