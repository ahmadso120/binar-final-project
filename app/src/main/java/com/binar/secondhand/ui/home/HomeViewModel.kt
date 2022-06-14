package com.binar.secondhand.ui.home

import androidx.lifecycle.*
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity

class HomeViewModel(
    private val buyerRepository: BuyerRepository
) : ViewModel() {

    private val _buyerProductsLiveData = buyerRepository.getBuyerProducts(null).asLiveData()
    val buyerProductsMediatorData = MediatorLiveData<Result<List<BuyerProductEntity>>>()
    private var _filterBuyerProductsLiveData: LiveData<Result<List<BuyerProductEntity>>>
    private val _updateCategoryIdLiveData = MutableLiveData<String>()

    init {
        _filterBuyerProductsLiveData = _updateCategoryIdLiveData.switchMap {
            buyerRepository.getBuyerProducts(it.toInt()).asLiveData()
        }

        buyerProductsMediatorData.addSource(_buyerProductsLiveData) {
            buyerProductsMediatorData.value = it
        }

        buyerProductsMediatorData.addSource(_filterBuyerProductsLiveData) {
            buyerProductsMediatorData.value = it
        }
    }

    fun onCategoryIdUpdated(categoryId: Int) {
        _updateCategoryIdLiveData.value = categoryId.toString()
    }
}