package com.binar.secondhand.ui.search

import android.util.Log
import androidx.lifecycle.*
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.SearchRepository
import com.binar.secondhand.data.source.local.entity.SearchHistory
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.Event

class SearchViewModel(
    private val searchRepository: SearchRepository,

) : ViewModel() {

    private val _search = MutableLiveData<String>()
    val search: LiveData<Result<List<BuyerProductResponse>>> = _search.switchMap {
        searchRepository.search(it)
    }

    fun getData(query:String){
        _search.value = query
        Log.d("search1",_search.toString())
    }
    private val _navigateToBuyerProductDetail = MutableLiveData<Event<BuyerProductResponse>>()
    val navigateToBuyerProductDetail: LiveData<Event<BuyerProductResponse>>
        get() = _navigateToBuyerProductDetail

    fun onBuyerProductClicked(buyerProductResponse: BuyerProductResponse) {
        _navigateToBuyerProductDetail.value = Event(buyerProductResponse)
    }

//    fun getSearchHistory(): LiveData<List<SearchHistory>> {
//        return searchRepository.getSearchHistory()
//    }
}