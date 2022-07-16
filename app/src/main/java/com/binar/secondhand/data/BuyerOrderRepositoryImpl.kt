package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.BuyerOrderRemoteDataSource
import com.binar.secondhand.data.source.remote.request.BidProductRequest
import com.binar.secondhand.data.source.remote.response.BuyerOrderResponse
import com.binar.secondhand.data.source.remote.response.DeleteResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

interface BuyerOrderRepository {
    suspend fun bidProduct(bidProductRequest: BidProductRequest) : Boolean
    fun getAllOrder(): LiveData<Result<List<BuyerOrderResponse>>>
    fun deleteOrder(id: Int): LiveData<Result<DeleteResponse>>
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

    override fun getAllOrder(): LiveData<Result<List<BuyerOrderResponse>>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = dataSource.getAllBuyerOrder()
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(it))
                    }
                } else {
                    loge("getOrder() => Request Error")
                    val error = response.errorBody()?.string()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                loge("getOrder() => ${e.message}")
                emit(Result.Error(null, "Something went wrong ${e.message}"))
            }
        }

    override fun deleteOrder(id: Int): LiveData<Result<DeleteResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = dataSource.deleteBuyerOrder(id)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(it))
                    }
                } else {
                    loge("deleteOrder() => Request Error")
                    val error = response.errorBody()?.string()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                loge("deleteOrder() => ${e.message}")
                emit(Result.Error(null, "Something went wrong"))
            }
        }

}