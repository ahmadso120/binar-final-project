package com.binar.secondhand.ui.sell

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.SellerCategoryRepository
import com.binar.secondhand.data.SellerProductRepository
import com.binar.secondhand.data.source.remote.request.SellerProductRequest
import com.binar.secondhand.data.source.remote.response.SellerProductResponse

class SellerViewModel(
    private val sellerProductRepository: SellerProductRepository,
    private val sellerCategoryRepository: SellerCategoryRepository
):ViewModel() {
    private val _sellerProductRequest = MutableLiveData<SellerProductRequest>()
    val addSellerProduct: LiveData<Result<SellerProductResponse>> = _sellerProductRequest.switchMap {
        sellerProductRepository.addProduct(it.file,it.partMap)
    }
    fun doAddSellerProductRequest(sellerProductRequest: SellerProductRequest){
        _sellerProductRequest.value = sellerProductRequest
    }

    val category=  sellerCategoryRepository.getCategories()

}