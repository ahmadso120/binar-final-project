package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.AccSettDataSource
import com.binar.secondhand.data.source.remote.request.AccountSettingRequest
import com.binar.secondhand.data.source.remote.response.AccountSettingResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject


interface AccSettRepo {
    fun getUser (): LiveData<Result<AccountSettingResponse>>
    fun putUser (body : AccountSettingRequest): LiveData<Result<AccountSettingResponse>>
}

class AccSettRepoImpl(
  private val source: AccSettDataSource
):AccSettRepo{
    override fun getUser(): LiveData<Result<AccountSettingResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = source.getUser()

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

    override fun putUser(body: AccountSettingRequest): LiveData<Result<AccountSettingResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = source.putUser(body)

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



