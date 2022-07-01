package com.binar.secondhand.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.binar.secondhand.data.source.remote.SellerProductDataSource
import com.binar.secondhand.data.source.remote.response.DeleteSellerProductResponse
import com.binar.secondhand.data.source.remote.response.SellerProductResponse
import com.binar.secondhand.utils.loge
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

interface SellerProductRepository{
    fun addProduct(
        file: MultipartBody.Part?,
        partMap: Map<String, RequestBody>): LiveData<Result<SellerProductResponse>
            >

    fun getProduct() : LiveData<Result<List<SellerProductResponse>>>

    fun deleteProduct(id:Int) : LiveData<Result<DeleteSellerProductResponse>>
    fun getProductDetail (id: Int) : LiveData<Result<SellerProductResponse>>
}

class SellerProductRepositoryImpl(
    private val sellerProductDataSource: SellerProductDataSource
): SellerProductRepository{
    override fun addProduct(
        file: MultipartBody.Part?,
        partMap: Map<String, RequestBody>
    ): LiveData<Result<SellerProductResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = sellerProductDataSource.addSellerProduct(file,partMap)
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(it))
                    }
                } else{
                    loge("addProduct() => Request Error")
                    val error = response.errorBody()?.string()
                    if(error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            } catch (e:Exception){
                loge("addProduct() => ${e.message}")
                emit(Result.Error(null, "Something went wrong"))
            }
        }

    override fun getProduct(): LiveData<Result<List<SellerProductResponse>>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = sellerProductDataSource.getSellerProduct()
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(it))
                    }
                } else{
                    loge("getProduct() => Request Error")
                    val error = response.errorBody()?.string()
                    if(error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            } catch (e:Exception){
                loge("getProduct() => ${e.message}")
                emit(Result.Error(null, "Something went wrong ${e.message}"))
            }
        }

    override fun deleteProduct(id: Int): LiveData<Result<DeleteSellerProductResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = sellerProductDataSource.deleteSellerProduct(id)
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(it))
                    }
                } else{
                    loge("deleteProduct() => Request Error")
                    val error = response.errorBody()?.string()
                    if(error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            } catch (e:Exception){
                loge("deleteProduct() => ${e.message}")
                emit(Result.Error(null, "Something went wrong"))
            }
        }

    override fun getProductDetail(id: Int): LiveData<Result<SellerProductResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = sellerProductDataSource.getProductDetail(id)
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let {
                        emit(Result.Success(it))
                    }
                } else{
                    loge("getDetailProduct() => Request Error")
                    val error = response.errorBody()?.string()
                    if(error != null){
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Result.Error(null, message))
                    }
                }
            } catch (e:Exception){
                loge("getDetailProduct() => ${e.message}")
                emit(Result.Error(null, "Something went wrong"))
            }
        }


}