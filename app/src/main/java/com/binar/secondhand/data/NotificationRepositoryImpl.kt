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
    fun getUnreadCount(): LiveData<Result<Int>>
    fun patchNotification(id: Int): LiveData<Result<NotificationResponseItem>>
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
                       val filtered = it.filter { item ->
                           item.product != null
                       }
                        emit(Result.Success(filtered))
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

    override fun getUnreadCount(): LiveData<Result<Int>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = notificationRemoteDataSource.getAllNotification()
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        val unRead = it.filter { item ->
                            !item.read && item.product != null
                        }.size
                        emit(Result.Success(unRead))
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

    override fun patchNotification(id: Int): LiveData<Result<NotificationResponseItem>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = notificationRemoteDataSource.patchNotification(id)
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(it))
                    }
                }else{
                    loge("patchNotification()=> Request error")
                    val error = response.errorBody()?.string()
                    if (error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            }catch (e: Exception){
                loge("patchNotification() => ${e.message}")
                emit(Result.Error(null, "Something went wrong"))
            }
        }
}