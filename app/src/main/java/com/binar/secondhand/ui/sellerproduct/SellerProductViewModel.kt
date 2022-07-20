package com.binar.secondhand.ui.sellerproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.SellerProductRepository
import com.binar.secondhand.data.source.remote.response.DeleteResponse
import com.binar.secondhand.data.source.remote.response.SellerProductResponse

class SellerProductViewModel(private val sellerProductRepository: SellerProductRepository): ViewModel() {


    fun getSellerProduct(): LiveData<Result<List<SellerProductResponse>>> {
        return sellerProductRepository.getProduct()
    }

    fun doDeleteProduct(id:Int): LiveData<Result<DeleteResponse>>{
        return sellerProductRepository.deleteProduct(id)
    }


}