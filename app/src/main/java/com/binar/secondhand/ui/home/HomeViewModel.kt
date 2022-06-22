package com.binar.secondhand.ui.home

import androidx.lifecycle.*
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.SellerCategoryRepository
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.utils.Event

class HomeViewModel(
    private val buyerRepository: BuyerRepository,
    sellerCategoryRepository: SellerCategoryRepository
) : ViewModel() {

    var categoryId: Int = 0
    private val _filterCategoryProduct = MutableLiveData(categoryId)

    val buyerProducts = _filterCategoryProduct.switchMap {
        buyerRepository.getBuyerProducts(it).asLiveData()
    }

    fun filterCategoryProduct(categoryId: Int) {
        _filterCategoryProduct.value = categoryId
    }

    val categories = sellerCategoryRepository.getCategories()

    private val _navigateToBuyerProductDetail = MutableLiveData<Event<BuyerProductEntity>>()
    val navigateToBuyerProductDetail: LiveData<Event<BuyerProductEntity>>
        get() = _navigateToBuyerProductDetail

    fun onBuyerProductClicked(buyerProductResponse: BuyerProductEntity) {
        _navigateToBuyerProductDetail.value = Event(buyerProductResponse)
    }
}