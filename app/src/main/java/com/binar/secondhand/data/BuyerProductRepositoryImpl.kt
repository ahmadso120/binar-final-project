package com.binar.secondhand.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.local.BuyerProductLocalDataSource
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.remote.BuyerProductRemoteDataSource
import com.binar.secondhand.data.source.remote.NetworkBoundResource
import com.binar.secondhand.data.source.remote.network.ApiResponse
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.AppExecutors
import com.binar.secondhand.utils.DataMapper
import com.binar.secondhand.utils.connection.HasInternetCapability
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject


interface BuyerRepository {
    fun getBuyerProducts(categoryId: Int): Flow<Result<List<BuyerProductEntity>>>
    fun getBuyerProductById(id: Int): LiveData<Result<BuyerProductResponse?>>
}

class BuyerRepositoryImpl(
    private val buyerProductLocalDataSource: BuyerProductLocalDataSource,
    private val buyerProductRemoteDataSource: BuyerProductRemoteDataSource,
    private val hasInternetCapability: HasInternetCapability,
    private val appExecutors: AppExecutors
) : BuyerRepository {

    override fun getBuyerProducts(categoryId: Int): Flow<Result<List<BuyerProductEntity>>> =
        object : NetworkBoundResource<List<BuyerProductEntity>, List<BuyerProductResponse>>() {
            override fun loadFromDB(): Flow<List<BuyerProductEntity>> {
                return buyerProductLocalDataSource.getBuyerProducts()
            }

            override fun shouldFetch(data: List<BuyerProductEntity>?): Boolean {
                if (!hasInternetCapability.isConnected) return false
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<List<BuyerProductResponse>>> =
                buyerProductRemoteDataSource.getBuyerProducts(categoryId)

            override suspend fun saveCallResult(data: List<BuyerProductResponse>?) {
                buyerProductLocalDataSource.deleteAllBuyerProduct()
                data?.let {
                    val buyerProductEntity = DataMapper.mapResponsesToBuyerProductEntities(data)
                    buyerProductLocalDataSource.insertBuyerProduct(buyerProductEntity)
                }
            }
        }.asFlow()

    override fun getBuyerProductById(id: Int): LiveData<Result<BuyerProductResponse?>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = buyerProductRemoteDataSource.getBuyerProductById(id)

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        emit(Result.Success(data))
                    } else {
                        emit(Result.Success(null))
                    }
                } else {
                    loge("getBuyerProductById() => Request error")
                    val error = response.errorBody()?.string()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                loge("getBuyerProductById() => ${e.message}")
                emit(Result.Error(null, "Something went wrong"))
            }
        }

}