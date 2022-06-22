package com.binar.secondhand.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.NotificationRemoteDataSource
import com.binar.secondhand.data.source.remote.response.NotificationResponseItem
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject


interface NotificatioRepository{
    fun getAllNotification(): LiveData<Result<List<NotificationResponseItem>>>
}

class NotificationRepositoryImpl(private val notificationRemoteDataSource: NotificationRemoteDataSource
):NotificatioRepository {
    override fun getAllNotification(): LiveData<Result<List<NotificationResponseItem>>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = notificationRemoteDataSource.getAllNotification()
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(it))
                    }
                }else{
                    loge("getNotification()=> Request error")
                    val error = response.errorBody()?.string()
                    if (error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            }catch (e: Exception){
                loge("getNotification() => ${e.message}")
                emit(Result.Error(null, "Something went wrong"))
            }
        }

}