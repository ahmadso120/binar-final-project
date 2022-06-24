package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.SellerOrderRemoteDataSource
import com.binar.secondhand.data.source.remote.response.SellerOrderResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

interface SellerOrderRepository {
    fun getSellerOrder(): LiveData<Result<List<SellerOrderResponse>>>
}
class SellerOrderRepositoryImpl(
    private val dataSource: SellerOrderRemoteDataSource
) : SellerOrderRepository {
    override fun getSellerOrder(): LiveData<Result<List<SellerOrderResponse>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = dataSource.getSellerOrder()
            if (response.isSuccessful) {
                val data = response.body()
                if (!data.isNullOrEmpty()) {
                    emit(Result.Success(data))
                } else {
                    emit(Result.Success(emptyList()))
                }
            } else {
                loge("login() => Request error")
                val error = response.errorBody()?.string()
                if (error != null) {
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    emit(Result.Error(null, message))
                }
            }
        } catch (e: Exception) {
            loge("login() => ${e.message}")
            emit(Result.Error(null, "Something went wrong"))
        }
    }
}