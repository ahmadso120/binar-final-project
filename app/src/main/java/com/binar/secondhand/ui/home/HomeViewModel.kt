package com.binar.secondhand.ui.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.SellerCategoryRepository
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.Event

class HomeViewModel(
    private val buyerRepository: BuyerRepository,
    sellerCategoryRepository: SellerCategoryRepository
) : ViewModel() {

    var categoryId: Int = 0
    private val _filterCategoryProduct = MutableLiveData(categoryId)

    val buyerProducts = _filterCategoryProduct.switchMap {
        buyerRepository.getBuyerProducts(it).cachedIn(viewModelScope)
    }

    fun filterCategoryProduct(categoryId: Int) {
        _filterCategoryProduct.value = categoryId
    }

    val categories = sellerCategoryRepository.getCategories()

    private val _navigateToBuyerProductDetail = MutableLiveData<Event<BuyerProductEntity>>()
    val navigateToBuyerProductDetail: LiveData<Event<BuyerProductEntity>>
        get() = _navigateToBuyerProductDetail

    fun onBuyerProductClicked(buyerProductEntity: BuyerProductEntity) {
        _navigateToBuyerProductDetail.value = Event(buyerProductEntity)
    }
}