package com.binar.secondhand.ui.home

import androidx.lifecycle.*
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.SellerCategoryRepository
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.utils.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val buyerRepository: BuyerRepository,
    sellerCategoryRepository: SellerCategoryRepository
) : ViewModel() {

    var categoryId: Int = 0
    val filterCategoryProduct = MutableStateFlow(categoryId)

    private val buyerProductsFlow = filterCategoryProduct.flatMapLatest {
        buyerRepository.getBuyerProducts(it)
    }

    val buyerProducts = buyerProductsFlow.asLiveData()

    val categories = sellerCategoryRepository.getCategories()

    private val _navigateToBuyerProductDetail = MutableLiveData<Event<BuyerProductEntity>>()
    val navigateToBuyerProductDetail: LiveData<Event<BuyerProductEntity>>
        get() = _navigateToBuyerProductDetail

    fun onBuyerProductClicked(buyerProductResponse: BuyerProductEntity) {
        _navigateToBuyerProductDetail.value = Event(buyerProductResponse)
    }
}