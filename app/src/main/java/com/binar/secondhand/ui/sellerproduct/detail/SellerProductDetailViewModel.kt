package com.binar.secondhand.ui.sellerproduct.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.binar.secondhand.data.AccountRepository
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.SellerProductRepository
import com.binar.secondhand.data.source.remote.response.AccountResponse
import com.binar.secondhand.data.source.remote.response.SellerProductResponse

class SellerProductDetailViewModel(
    private val sellerProductRepository: SellerProductRepository,
    private val accountRepository: AccountRepository): ViewModel(){

        fun getProductDetail(id: Int): LiveData<Result<SellerProductResponse>>{
            return sellerProductRepository.getProductDetail(id)
        }

        fun getSellerName(): LiveData<Result<AccountResponse>>{
            return accountRepository.getAccount()
        }
}