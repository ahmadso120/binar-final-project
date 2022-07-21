package com.binar.secondhand.ui.productdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.BuyerOrderRepository
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.WishlistRepository
import com.binar.secondhand.data.source.remote.request.BidProductRequest
import com.binar.secondhand.data.source.remote.request.WishlistRequest

class ProductDetailViewModel(
    buyerProductRepository: BuyerRepository,
    private val orderRepository: BuyerOrderRepository,
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    private val _setIdProduct = MutableLiveData<Int>()

    val getProductDetail = _setIdProduct.switchMap {
        buyerProductRepository.getBuyerProductById(it)
    }

    val getProductIsWishlist = _setIdProduct.switchMap {
        wishlistRepository.getWishlistByProductId(it)
    }

    fun setIdProduct(id: Int) {
        _setIdProduct.value = id
    }

    private val _setIdProductForWishlist = MutableLiveData<Int>()

    val checkProductIsWishlist = _setIdProductForWishlist.switchMap {
        wishlistRepository.getWishlistByProductId(it)
    }

    fun setIdProductForWishlist(id: Int) {
        _setIdProductForWishlist.value = id
    }

    suspend fun wishlist(wishlistRequest: WishlistRequest): Boolean {
        return wishlistRepository.wishlist(wishlistRequest)
    }

    suspend fun deleteWishlist(id: Int): Boolean {
        return wishlistRepository.deleteWishlist(id)
    }

    val hasProductOrdered = _setIdProduct.switchMap {
        orderRepository.hasProductOrdered(it)
    }

    suspend fun setBidPrice(bidProductRequest: BidProductRequest): Boolean {
        return orderRepository.bidProduct(bidProductRequest)
    }
}