package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.request.AddSellerProductRequest
import com.binar.secondhand.data.source.remote.request.RegisterRequest
import com.binar.secondhand.data.source.remote.response.DeleteSellerProductResponse
import com.binar.secondhand.data.source.remote.response.RegisterResponse
import com.binar.secondhand.data.source.remote.response.SellerProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SellerProductService {

    @Multipart
    @JvmSuppressWildcards
    @POST("seller/product")
    suspend fun addProduct(
        @Part file: MultipartBody.Part?,
        @PartMap partMap: Map<String, RequestBody>,
    ): Response<SellerProductResponse>

    @GET("seller/product")
    suspend fun getSellerProduct() : Response<List<SellerProductResponse>>

    @DELETE("seller/product/{id}")
    suspend fun deleteSellerProduct(
        @Path("id") id : Int
    ): Response<DeleteSellerProductResponse>

    @GET("seller/product/{id}")
    suspend fun getSellerProductDetail(
        @Path("id") id : Int
    ): Response<SellerProductResponse>
}