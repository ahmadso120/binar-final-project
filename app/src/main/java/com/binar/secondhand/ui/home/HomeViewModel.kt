package com.binar.secondhand.ui.home

import androidx.lifecycle.*
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.SellerCategoryRepository
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.local.entity.BuyerProductWithCategories
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.Event

class HomeViewModel(
    private val buyerRepository: BuyerRepository,
    sellerCategoryRepository: SellerCategoryRepository
) : ViewModel() {

    var categoryId: Int = 0

    private val _filterCategoryProductLiveData = MutableLiveData(categoryId)

    val buyerProductsLiveData: LiveData<Result<List<BuyerProductWithCategories>>> = _filterCategoryProductLiveData.switchMap {
        buyerRepository.getBuyerProducts(it).asLiveData()
    }

    fun filterCategoryProductHome(categoryId: Int) {
        _filterCategoryProductLiveData.value = categoryId
    }

    val categories = sellerCategoryRepository.getCategories()

    private val _navigateToBuyerProductDetail = MutableLiveData<Event<BuyerProductWithCategories>>()
    val navigateToBuyerProductDetail: LiveData<Event<BuyerProductWithCategories>>
        get() = _navigateToBuyerProductDetail

    fun onBuyerProductClicked(buyerProductResponse: BuyerProductWithCategories) {
        _navigateToBuyerProductDetail.value = Event(buyerProductResponse)
    }
}