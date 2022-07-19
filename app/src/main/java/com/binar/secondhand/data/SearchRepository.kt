package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.local.SearchHistoryDataSource
import com.binar.secondhand.data.source.local.entity.SearchHistory
import com.binar.secondhand.data.source.remote.SearchDataSource
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

interface SearchRepository {
    fun search (query : String): LiveData<Result<List<BuyerProductResponse>>>
    suspend fun getSearchHistory(): List<SearchHistory>
    suspend fun addSearchHistory(history:SearchHistory):Long
}

class SearchRepositoryImpl(
    private val searchDataSource: SearchDataSource,
    private val searchHistoryDataSource: SearchHistoryDataSource
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

    override suspend fun getSearchHistory(): List<SearchHistory>  {
        return searchHistoryDataSource.getSearchHistory()
    }

    override suspend fun addSearchHistory(history: SearchHistory):Long {
        return searchHistoryDataSource.insert(history)
    }

}