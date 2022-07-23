package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.local.SearchHistoryDataSource
import com.binar.secondhand.data.source.local.entity.SearchHistory
import com.binar.secondhand.data.source.remote.SearchDataSource
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject


interface SearchRepository {
    fun search (query : String,status: String): LiveData<Result<List<BuyerProductResponse>>>
    fun getSearchHistory(): Flow<List<SearchHistory>>
    suspend fun addSearchHistory(history:SearchHistory):Long
    suspend fun  deleteAllHistory()
    suspend fun delete(historySearch : SearchHistory)
}

class SearchRepositoryImpl(
    private val searchDataSource: SearchDataSource,
    private val searchHistoryDataSource: SearchHistoryDataSource
):SearchRepository{
    override fun search(query: String,status:String):LiveData<Result<List<BuyerProductResponse>>> = liveData(Dispatchers.IO){
        emit(Result.Loading)
        try {
            val response = searchDataSource.searchQuery(query,status)

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



    override fun getSearchHistory(): Flow<List<SearchHistory>>   {
        return searchHistoryDataSource.getSearchHistory()
    }

    override suspend fun addSearchHistory(history: SearchHistory):Long {
        return searchHistoryDataSource.insert(history)
    }

    override suspend fun deleteAllHistory() {
        return searchHistoryDataSource.deleteHistory()
    }

    override suspend fun delete(historySearch : SearchHistory) {
        return searchHistoryDataSource.deleteHistoryId(historySearch)
    }

}