package com.binar.secondhand.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.AccountRemoteDataSource
import com.binar.secondhand.data.source.remote.response.AccountResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject


interface AccountRepository {
    fun getAccount(): LiveData<Result<AccountResponse>>
}

class AccountRepositoryImpl(
    private val accountRemoteDataSource: AccountRemoteDataSource
) : AccountRepository {
    override fun getAccount(): LiveData<Result<AccountResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = accountRemoteDataSource.getAccount()

                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(it))
                    }
                }else{
                    loge("getAccount()=> Request error")
                    val error = response.errorBody()?.string()
                    if (error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            } catch (e: Exception){
                loge("getAccount()=> ${e.message}")
                emit(Result.Error(null, "Something went wrong"))
            }
        }


}