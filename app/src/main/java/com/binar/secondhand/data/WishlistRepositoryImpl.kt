package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.WishlistRemoteDataSource
import com.binar.secondhand.data.source.remote.request.WishlistRequest
import com.binar.secondhand.data.source.remote.response.WishlistResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

interface WishlistRepository {
    suspend fun wishlist(wishlistRequest: WishlistRequest): Boolean
    suspend fun deleteWishlist(productId: Int): Boolean
    fun getWishlistByProductId(productId: Int): LiveData<Result<List<WishlistResponse>?>>
}

class WishlistRepositoryImpl(
    private val dataSource: WishlistRemoteDataSource
) : WishlistRepository {
    override suspend fun wishlist(wishlistRequest: WishlistRequest): Boolean {
        return try {
            dataSource.wishlist(wishlistRequest).isSuccessful
        } catch (e: Exception) {
            loge("wishlist() => ${e.message}")
            false
        }
    }

    override suspend fun deleteWishlist(id: Int): Boolean {
        return try {
            dataSource.deleteWishlist(id).isSuccessful
        } catch (e: Exception) {
            loge("deleteWishlist() => ${e.message}")
            false
        }
    }

    override fun getWishlistByProductId(productId: Int): LiveData<Result<List<WishlistResponse>?>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = dataSource.getWishlistByProductId()
            if (response.isSuccessful) {
                val data = response.body()
                val filteredData = data?.filter {
                    it.productId == productId
                }
                if (!filteredData.isNullOrEmpty()) {
                    emit(Result.Success(filteredData))
                } else {
                    emit(Result.Success(emptyList()))
                }
            } else {
                loge("getWishlist() => Request error")
                val error = response.errorBody()?.string()
                if (error != null) {
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    emit(Result.Error(null, message))
                }
            }
        } catch (e: Exception) {
            loge("getWishlist() => ${e.message}")
            emit(Result.Error(null, "Something went wrong"))
        }
    }
}