package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<Result<ResultType>> = flow {
        emit(Result.Loading)
        val dbSource = loadFromDB().first()
        if (shouldFetch(dbSource)) {
            emit(Result.Loading)
            when (val apiResponse = createCall().first()) {
                is ApiResponse.Success -> {
                    saveCallResult(apiResponse.data)
                    emitAll(loadFromDB().map { Result.Success(it) })
                }
                is ApiResponse.Empty -> {
                    saveCallResult(null)
                    emitAll(loadFromDB().map { Result.Success(it) })
                }
                is ApiResponse.Error -> {
                    onFetchFailed()
                    emit(Result.Error(null, apiResponse.errorMessage))
                }
            }
        } else {
            emitAll(loadFromDB().map { Result.Success(it) })
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType?)

    fun asFlow(): Flow<Result<ResultType>> = result
}