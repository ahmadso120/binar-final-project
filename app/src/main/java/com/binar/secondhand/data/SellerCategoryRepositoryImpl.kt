package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.SellerCategoryDataSource
import com.binar.secondhand.data.source.remote.response.CategoryResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

interface SellerCategoryRepository {
    fun getCategories(): LiveData<Result<List<CategoryResponse>>>
}

class SellerCategoryRepositoryImpl(
    private val sellerCategoryDataSource: SellerCategoryDataSource
) : SellerCategoryRepository {
    override fun getCategories(): LiveData<Result<List<CategoryResponse>>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = sellerCategoryDataSource.getCategories()

                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(it))
                    }
                } else {
                    loge("login() => Request error")
                    val error = response.errorBody()?.string()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                loge("login() => ${e.message}")
                emit(Result.Error(null, "Something went wrong"))
            }
        }

}