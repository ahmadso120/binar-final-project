package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.ApiResponse
import com.binar.secondhand.data.source.remote.network.ApiService
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.logd
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BuyerRemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun getBuyerProducts(categoryId: Int? = null): Flow<ApiResponse<List<BuyerProductResponse>>> {
        return flow {
            try {
                val data: List<BuyerProductResponse> = if (categoryId != null){
                    apiService.getBuyerProductByCategory(categoryId)
                } else {
                    apiService.getBuyerProduct()
                }

                if (data.isNotEmpty()) {
                    logd("getBuyerProducts() => $data")
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                loge("getBuyerProducts() => $e")
            }
        }.flowOn(Dispatchers.IO)
    }
}