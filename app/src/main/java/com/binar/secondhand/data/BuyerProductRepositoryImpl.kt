package com.binar.secondhand.data

import com.binar.secondhand.data.source.local.BuyerProductLocalDataSource
import com.binar.secondhand.data.source.local.entity.BuyerProductWithCategories
import com.binar.secondhand.data.source.remote.BuyerProductRemoteDataSource
import com.binar.secondhand.data.source.remote.NetworkBoundResource
import com.binar.secondhand.data.source.remote.network.ApiResponse
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.AppExecutors
import com.binar.secondhand.utils.DataMapper
import com.binar.secondhand.utils.RateLimiter
import com.binar.secondhand.utils.connection.HasInternetCapability
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

interface BuyerRepository {
    fun getBuyerProducts(categoryId: Int): Flow<Result<List<BuyerProductWithCategories>>>
}

class BuyerRepositoryImpl(
    private val buyerProductLocalDataSource: BuyerProductLocalDataSource,
    private val buyerProductRemoteDataSource: BuyerProductRemoteDataSource,
    private val hasInternetCapability: HasInternetCapability,
    private val appExecutors: AppExecutors
) : BuyerRepository {

    private val rateLimiter = RateLimiter<String>(5, TimeUnit.MINUTES)

    override fun getBuyerProducts(categoryId: Int): Flow<Result<List<BuyerProductWithCategories>>> =
        object : NetworkBoundResource<List<BuyerProductWithCategories>, List<BuyerProductResponse>>() {
            override fun loadFromDB(): Flow<List<BuyerProductWithCategories>> {
                return buyerProductLocalDataSource.getBuyerProducts()
            }

            override fun shouldFetch(data: List<BuyerProductWithCategories>?): Boolean {
                if (categoryId != DEFAULT_CATEGORY_ID) return true
                if (!hasInternetCapability.isConnected) return false
                return data.isNullOrEmpty()
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

//            override fun onFetchFailed() {
//                super.onFetchFailed()
//                rateLimiter.shouldFetch(LIST_BUYER_PRODUCT)
//            }
        }.asFlow()

    companion object {
        private const val LIST_BUYER_PRODUCT = "LIST_BUYER_PRODUCT"
        private const val DEFAULT_CATEGORY_ID = 0
    }
}