package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.AuthRemoteDataSource
import com.binar.secondhand.data.source.remote.request.LoginRequest
import com.binar.secondhand.data.source.remote.request.RegisterRequest
import com.binar.secondhand.data.source.remote.response.LoginResponse
import com.binar.secondhand.data.source.remote.response.RegisterResponse
import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.storage.UserLoggedIn
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers

import okhttp3.MultipartBody
import okhttp3.RequestBody

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import org.json.JSONObject

interface AuthRepository {
    fun login(loginRequest: LoginRequest): LiveData<Result<LoginResponse>>
    fun setUserLoggedIn(userLoggedIn: UserLoggedIn)
    fun register(registerRequest:RegisterRequest): LiveData<Result<RegisterResponse>>
    fun isUserHasLoggedIn(): Flow<Boolean>


}

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val appLocalData: AppLocalData,
) : AuthRepository {
    override fun login(loginRequest: LoginRequest): LiveData<Result<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = authRemoteDataSource.login(loginRequest)

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

    override fun setUserLoggedIn(userLoggedIn: UserLoggedIn) =
        appLocalData.setUserLoggedIn(userLoggedIn)

    override fun isUserHasLoggedIn(): Flow<Boolean> = flow {
        val isUserHasLoggedIn = appLocalData.isUserHasLoggedIn
        emit(isUserHasLoggedIn)
    }

    override fun register(registerRequest:RegisterRequest): LiveData<Result<RegisterResponse>> =
    liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = authRemoteDataSource.register(registerRequest)
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