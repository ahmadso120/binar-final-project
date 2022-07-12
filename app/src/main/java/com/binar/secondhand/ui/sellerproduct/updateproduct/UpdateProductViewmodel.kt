package com.binar.secondhand.ui.sellerproduct.updateproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.SellerCategoryRepository
import com.binar.secondhand.data.SellerProductRepository
import com.binar.secondhand.data.source.remote.request.SellerProductRequest
import com.binar.secondhand.data.source.remote.response.SellerProductResponse

class UpdateProductViewmodel(
    private val sellerProductRepository: SellerProductRepository,
    sellerCategoryRepository: SellerCategoryRepository
): ViewModel(){

/*
    private val _sellerProductRequest = MutableLiveData<SellerProductRequest>()
    val updateProduct: LiveData<Result<SellerProductResponse>> = _sellerProductRequest.switchMap {
        sellerProductRepository.updateProduct(it.file,it.partMap)
    }
    fun doUpdateSellerProductRequest(sellerProductRequest: SellerProductRequest,id: Int){
        _sellerProductRequest.value = sellerProductRequest
    }
*/


    fun getProductDetail(id: Int): LiveData<Result<SellerProductResponse>> {
        return sellerProductRepository.getProductDetail(id)
    }
    val category=  sellerCategoryRepository.getCategories()

    fun doUpdateProduct(sellerProductRequest: SellerProductRequest, id: Int): LiveData<Result<SellerProductResponse>>{
      return sellerProductRepository.updateProduct(
            sellerProductRequest.file,
            sellerProductRequest.partMap,
            id
        )
    }

}