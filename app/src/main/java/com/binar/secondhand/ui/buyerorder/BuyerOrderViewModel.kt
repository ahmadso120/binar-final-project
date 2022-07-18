package com.binar.secondhand.ui.buyerorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.binar.secondhand.data.BuyerOrderRepository
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.RebidBuyerOrderRequest
import com.binar.secondhand.data.source.remote.response.BuyerOrderResponse
import com.binar.secondhand.data.source.remote.response.DeleteResponse

class BuyerOrderViewModel(private val buyerOrderRepository: BuyerOrderRepository): ViewModel() {

    fun getAllOrder () : LiveData<Result<List<BuyerOrderResponse>>>{
        return buyerOrderRepository.getAllOrder()
    }

    fun deleteOrder(id:Int): LiveData<Result<DeleteResponse>>{
        return buyerOrderRepository.deleteOrder(id)
    }

    fun updateBidPrice(id:Int,rebidBuyerOrderRequest: RebidBuyerOrderRequest):LiveData<Result<BuyerOrderResponse>>{
        return buyerOrderRepository.updateBidPrice(id,rebidBuyerOrderRequest)
    }
}