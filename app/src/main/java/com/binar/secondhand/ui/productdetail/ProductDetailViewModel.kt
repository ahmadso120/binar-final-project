package com.binar.secondhand.ui.productdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.BuyerOrderRepository
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.source.remote.request.BidProductRequest

class ProductDetailViewModel(
    buyerProductRepository: BuyerRepository,
    private val orderRepository: BuyerOrderRepository,
) : ViewModel() {

    private val _setIdProduct = MutableLiveData<Int>()

    val getProductDetail = _setIdProduct.switchMap {
        buyerProductRepository.getBuyerProductById(it)
    }

    fun setIdProduct(id: Int) {
        _setIdProduct.value = id
    }

    val hasProductOrdered = _setIdProduct.switchMap {
        orderRepository.hasProductOrdered(it)
    }

    suspend fun setBidPrice(bidProductRequest: BidProductRequest): Boolean {
        return orderRepository.bidProduct(bidProductRequest)
    }
}