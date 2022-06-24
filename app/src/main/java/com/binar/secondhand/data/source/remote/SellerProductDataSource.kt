package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.SellerProductService
import com.binar.secondhand.data.source.remote.request.AddSellerProductRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SellerProductDataSource(
    private val sellerProductService: SellerProductService
) {
    suspend fun addSellerProduct(file: MultipartBody.Part?, partMap: Map<String, RequestBody>) =

        sellerProductService.addProduct(file,partMap)
}