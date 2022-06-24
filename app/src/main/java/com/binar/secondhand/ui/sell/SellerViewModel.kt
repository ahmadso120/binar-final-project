package com.binar.secondhand.ui.sell

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.SellerCategoryRepository
import com.binar.secondhand.data.SellerProductRepository
import com.binar.secondhand.data.source.remote.request.AddSellerProductRequest
import com.binar.secondhand.data.source.remote.request.RegisterRequest
import com.binar.secondhand.data.source.remote.response.RegisterResponse
import com.binar.secondhand.data.source.remote.response.SellerProductResponse

class SellerViewModel(
    private val sellerProductRepository: SellerProductRepository,
    private val sellerCategoryRepository: SellerCategoryRepository
):ViewModel() {
    private val _sellerProductRequest = MutableLiveData<AddSellerProductRequest>()
    val addSellerProduct: LiveData<Result<SellerProductResponse>> = _sellerProductRequest.switchMap {
        sellerProductRepository.addProduct(it.file,it.partMap)
    }
    fun doAddSellerProductRequest(addSellerProductRequest: AddSellerProductRequest){
        _sellerProductRequest.value = addSellerProductRequest
    }

    val category=  sellerCategoryRepository.getCategories()

}