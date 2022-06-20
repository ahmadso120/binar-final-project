package com.binar.secondhand.data.source.local.room

import androidx.room.*
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.local.entity.BuyerProductWithCategories
import com.binar.secondhand.data.source.local.entity.CategoryBuyerProductCrossRef
import com.binar.secondhand.data.source.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyerProductDao {
    @Transaction
    @Query("SELECT * FROM buyer_product ORDER BY buyer_product.buyerProductId DESC")
    fun getBuyerProducts(): Flow<List<BuyerProductWithCategories>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryBuyerProductCrossRef(
        categoryBuyerProductCrossRef: List<CategoryBuyerProductCrossRef>
    )

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(data: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBuyerProduct(data: List<BuyerProductEntity>)

    @Query("DELETE FROM CategoryBuyerProductCrossRef")
    suspend fun deleteAllCategoryBuyerProductCrossRef()

    @Query("DELETE FROM buyer_product")
    suspend fun deleteAllBuyerProduct()

    @Query("DELETE FROM category")
    suspend fun deleteAllCategory()
}