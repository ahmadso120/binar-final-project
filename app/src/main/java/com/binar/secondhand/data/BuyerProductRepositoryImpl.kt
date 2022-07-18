package com.binar.secondhand.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.binar.secondhand.data.source.local.BuyerProductLocalDataSource
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.local.room.AppDatabase
import com.binar.secondhand.data.source.remote.BuyerProductRemoteDataSource
import com.binar.secondhand.data.source.remote.NetworkBoundResource
import com.binar.secondhand.data.source.remote.network.ApiResponse
import com.binar.secondhand.data.source.remote.network.BuyerProductService
import com.binar.secondhand.data.source.remote.response.BuyerProductDetailResponse
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.AppExecutors
import com.binar.secondhand.utils.DataMapper
import com.binar.secondhand.utils.connection.HasInternetCapability
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject


interface BuyerRepository {
    fun getBuyerProducts(categoryId: Int): LiveData<PagingData<BuyerProductEntity>>
    fun getBuyerProductById(id: Int): LiveData<Result<BuyerProductDetailResponse?>>
}

class BuyerRepositoryImpl(
    private val database: AppDatabase,
    private val service: BuyerProductService,
    private val buyerProductRemoteDataSource: BuyerProductRemoteDataSource
) : BuyerRepository {


    override fun getBuyerProducts(categoryId: Int): LiveData<PagingData<BuyerProductEntity>> {
        val catId = if (categoryId == 0) null else categoryId
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = BuyerProductRemoteMediator(database, service, catId),
            pagingSourceFactory = {
                database.buyerProductDao().getBuyerProducts()
            }
        ).liveData
    }

    override fun getBuyerProductById(id: Int): LiveData<Result<BuyerProductDetailResponse?>> =
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