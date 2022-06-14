package com.binar.secondhand.data

import com.binar.secondhand.data.source.local.BuyerLocalDataSource
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.remote.BuyerRemoteDataSource
import com.binar.secondhand.data.source.remote.NetworkBoundResource
import com.binar.secondhand.data.source.remote.network.ApiResponse
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.AppExecutors
import com.binar.secondhand.utils.DataMapper
import kotlinx.coroutines.flow.Flow

interface BuyerRepository {
    fun getBuyerProducts(categoryId: Int? = null): Flow<Result<List<BuyerProductEntity>>>
}

class BuyerRepositoryImpl(
    private val buyerLocalDataSource: BuyerLocalDataSource,
    private val buyerRemoteDataSource: BuyerRemoteDataSource,
    private val appExecutors: AppExecutors
) : BuyerRepository {
    override fun getBuyerProducts(categoryId: Int?): Flow<Result<List<BuyerProductEntity>>> =
        object : NetworkBoundResource<List<BuyerProductEntity>, List<BuyerProductResponse>>() {
            override fun loadFromDB(): Flow<List<BuyerProductEntity>> {
                return buyerLocalDataSource.getBuyerProducts()
            }

            override fun shouldFetch(data: List<BuyerProductEntity>?): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<List<BuyerProductResponse>>> =
                buyerRemoteDataSource.getBuyerProducts(categoryId)

            override suspend fun saveCallResult(data: List<BuyerProductResponse>) {
                val dataList = DataMapper.mapResponsesToEntities(data)
                buyerLocalDataSource.insertBuyerProducts(dataList)
            }
        }.asFlow()
}