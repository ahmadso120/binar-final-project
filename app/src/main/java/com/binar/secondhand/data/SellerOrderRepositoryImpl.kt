package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.SellerOrderRemoteDataSource
import com.binar.secondhand.data.source.remote.response.SellerOrderResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import okhttp3.RequestBody
import org.json.JSONObject

interface SellerOrderRepository {
    fun getSellerOrder(hasSold: Boolean): LiveData<Result<List<SellerOrderResponse>>>
    fun getSellerOrderById(id: Int): LiveData<Result<SellerOrderResponse>>
    suspend fun updateStatusOrder(id: Int, status: RequestBody): Boolean
}

class SellerOrderRepositoryImpl(
    private val dataSource: SellerOrderRemoteDataSource
) : SellerOrderRepository {
    override fun getSellerOrder(hasSold: Boolean): LiveData<Result<List<SellerOrderResponse>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = dataSource.getSellerOrder()
            if (response.isSuccessful) {
                val data = response.body()
                val status = if (hasSold) "accepted" else "pending"
                val filteredData = data?.filter {
                    it.status == status
                }
                if (!filteredData.isNullOrEmpty()) {
                    emit(Result.Success(filteredData))
                } else {
                    emit(Result.Success(emptyList()))
                }
            } else {
                loge("getSellerOrder() => Request error")
                val error = response.errorBody()?.string()
                if (error != null) {
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    emit(Result.Error(null, message))
                }
            }
        } catch (e: Exception) {
            loge("getSellerOrder() => ${e.message}")
            emit(Result.Error(null, "Something went wrong"))
        }
    }

    override fun getSellerOrderById(id: Int): LiveData<Result<SellerOrderResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = dataSource.getSellerOrderById(id)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    emit(Result.Success(data))
                }
            } else {
                loge("getSellerOrderById() => Request error")
                val error = response.errorBody()?.string()
                if (error != null) {
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    emit(Result.Error(null, message))
                }
            }
        } catch (e: Exception) {
            loge("getSellerOrderById() => ${e.message}")
            emit(Result.Error(null, "Something went wrong"))
        }
    }

    override suspend fun updateStatusOrder(id: Int, status: RequestBody) : Boolean {
        return try {
            val response = dataSource.updateStatusOrder(id, status)
            response.isSuccessful
        } catch (e: Exception) {
            loge("updateStatusOrder() => ${e.message}")
            false
        }
    }
}