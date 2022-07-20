package com.binar.secondhand.ui.sellerorder

import androidx.lifecycle.*
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.SellerOrderRepository
import com.binar.secondhand.data.source.remote.response.SellerOrderResponse
import com.binar.secondhand.utils.Event
import okhttp3.RequestBody

class SellerOrderViewModel(
    private val repository: SellerOrderRepository
) : ViewModel() {

    val interestedOrder = repository.getSellerOrder(false)
    val soldOrder = repository.getSellerOrder(true)

    private val _navigateToBidderInfo = MutableLiveData<Event<SellerOrderResponse>>()
    val navigateToBidderInfo: LiveData<Event<SellerOrderResponse>>
        get() = _navigateToBidderInfo

    fun onOrderClicked(item: SellerOrderResponse) {
        _navigateToBidderInfo.value = Event(item)
    }

    suspend fun onUpdateStatusClicked(state: UpdateStatus) : Boolean{
        return repository.updateStatusOrder(state.id, state.status)
    }

    private val _setOrderId = MutableLiveData<Int>()

    fun getData(id: Int) {
        _setOrderId.value = id
    }

    val getData = _setOrderId.switchMap {
        repository.getSellerOrderById(it)
    }

    data class UpdateStatus (
        val id: Int,
        val status: RequestBody
    )
}