package com.binar.secondhand.data

import com.binar.secondhand.data.source.local.BuyerProductLocalDataSource
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.remote.BuyerProductRemoteDataSource
import com.binar.secondhand.data.source.remote.NetworkBoundResource
import com.binar.secondhand.data.source.remote.network.ApiResponse
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.AppExecutors
import com.binar.secondhand.utils.DataMapper
import com.binar.secondhand.utils.connection.HasInternetCapability
import kotlinx.coroutines.flow.Flow

interface BuyerRepository {
    fun getBuyerProducts(categoryId: Int): Flow<Result<List<BuyerProductEntity>>>
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
}