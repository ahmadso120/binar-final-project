package com.binar.secondhand.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.HistoryDataSource
import kotlinx.coroutines.Dispatchers
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.response.HistoryResponseItem
import com.binar.secondhand.utils.loge
import org.json.JSONObject

interface HistoryRepository{
    fun history():LiveData<Result<List<HistoryResponseItem>>>
}
class HistoryRepositoryImpl(private val historyDataSource: HistoryDataSource):HistoryRepository {
    override fun history(): LiveData<Result<List<HistoryResponseItem>>> =
        liveData(Dispatchers.IO){
            emit(Result.Loading)
            try {
                val response = historyDataSource.history()
                if(response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(data))
                    }
                }else{
                    loge("getHistory()=> Request error")
                    val error = response.errorBody()?.string()
                    if (error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            }catch (e: Exception){
                loge("getHistory() => ${e.message}")
                emit(Result.Error(null, "Something went wrong"))
            }
        }

}