package com.binar.secondhand.data

import com.binar.secondhand.data.source.local.BuyerProductLocalDataSource
import com.binar.secondhand.data.source.local.entity.BuyerProductWithCategories
import com.binar.secondhand.data.source.remote.BuyerProductRemoteDataSource
import com.binar.secondhand.data.source.remote.NetworkBoundResource
import com.binar.secondhand.data.source.remote.network.ApiResponse
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.AppExecutors
import com.binar.secondhand.utils.DataMapper
import kotlinx.coroutines.flow.Flow

interface BuyerRepository {
    fun getBuyerProducts(categoryId: Int): Flow<Result<List<BuyerProductWithCategories>>>
}

class BuyerRepositoryImpl(
    private val buyerProductLocalDataSource: BuyerProductLocalDataSource,
    private val buyerProductRemoteDataSource: BuyerProductRemoteDataSource,
    private val appExecutors: AppExecutors
) : BuyerRepository {
    override fun getBuyerProducts(categoryId: Int): Flow<Result<List<BuyerProductWithCategories>>> =
        object : NetworkBoundResource<List<BuyerProductWithCategories>, List<BuyerProductResponse>>() {
            override fun loadFromDB(): Flow<List<BuyerProductWithCategories>> {
                return buyerProductLocalDataSource.getBuyerProducts()
            }

            override fun shouldFetch(data: List<BuyerProductWithCategories >?): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<List<BuyerProductResponse>>> =
                buyerProductRemoteDataSource.getBuyerProducts(categoryId)

            override suspend fun saveCallResult(data: List<BuyerProductResponse>?) {
                buyerProductLocalDataSource.deleteAllBuyerProduct()
                buyerProductLocalDataSource.deleteAllCategoryBuyerProductCrossRef()
                buyerProductLocalDataSource.deleteAllCategory()
                data?.let {
                    val buyerProductEntity = DataMapper.mapResponsesToBuyerProductEntities(data)
                    val categoryEntity = DataMapper.mapResponsesToCategoryEntities(data)
                    val categoryBuyerProductCrossRef = DataMapper.mapResponsesToCategoryBuyerProductCrossRef(data)
                    buyerProductLocalDataSource.insertBuyerProduct(buyerProductEntity)
                    buyerProductLocalDataSource.insertCategories(categoryEntity)
                    buyerProductLocalDataSource.insertCategoryBuyerProductCrossRef(categoryBuyerProductCrossRef)
                }
            }
        }.asFlow()
}