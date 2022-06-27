package com.binar.secondhand.ui.sellerorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.binar.secondhand.data.SellerOrderRepository
import com.binar.secondhand.data.source.remote.response.SellerOrderResponse
import com.binar.secondhand.utils.Event

class SellerOrderViewModel(
    private val repository: SellerOrderRepository
) : ViewModel() {

    val sellerOrder = repository.getSellerOrder()

    private val _navigateToBidderInfo = MutableLiveData<Event<SellerOrderResponse>>()
    val navigateToBidderInfo: LiveData<Event<SellerOrderResponse>>
        get() = _navigateToBidderInfo

    fun onOrderClicked(item: SellerOrderResponse) {
        _navigateToBidderInfo.value = Event(item)
    }

}