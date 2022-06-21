package com.binar.secondhand.data.source.remote

import androidx.lifecycle.LiveData
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.network.ApiResponse
import com.binar.secondhand.data.source.remote.network.AuthService
import com.binar.secondhand.data.source.remote.network.BuyerProductService
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.logd
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class BuyerProductRemoteDataSource(
    private val buyerProductService: BuyerProductService
) {
    suspend fun getBuyerProducts(categoryId: Int): Flow<ApiResponse<List<BuyerProductResponse>>> {
        return flow {
            try {
                val data: List<BuyerProductResponse> = if (categoryId > 0){
                    buyerProductService.getBuyerProductByCategory(categoryId)
                } else {
                    buyerProductService.getBuyerProduct()
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