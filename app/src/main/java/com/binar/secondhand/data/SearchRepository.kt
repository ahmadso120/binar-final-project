package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.SearchDataSource
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

interface SearchRepository {
    fun search (query : String): LiveData<Result<List<BuyerProductResponse>>>
}

class SearchRepositoryImpl(
    private val searchDataSource: SearchDataSource
):SearchRepository{
    override fun search(query: String):LiveData<Result<List<BuyerProductResponse>>> = liveData(Dispatchers.IO){
        emit(Result.Loading)
        try {
            val response = searchDataSource.searchQuery(query)

            if (response.isSuccessful){
                val data = response.body()
                data?.let {
                    emit(Result.Success(it))
                }
            }else{
                loge("getUser()=> Request error")
                val error = response.errorBody()?.string()
                if (error != null){
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    emit(Result.Error(null, message))
                }
            }
        }catch (e: Exception){
            loge("getUser()=> ${e.message}")
            emit(Result.Error(null, "Something went wrong"))
        }
    }

}