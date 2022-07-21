package com.binar.secondhand.ui.search


import androidx.lifecycle.*
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.SearchRepository
import com.binar.secondhand.data.source.local.entity.SearchHistory
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,

) : ViewModel() {

    private val _search = MutableLiveData<String>()
    val search: LiveData<Result<List<BuyerProductResponse>>> = _search.switchMap {
        searchRepository.search(it)
    }

    fun getData(query:String){
        _search.value = query

    }
    private val _navigateToBuyerProductDetail = MutableLiveData<Event<BuyerProductResponse>>()
    val navigateToBuyerProductDetail: LiveData<Event<BuyerProductResponse>>
        get() = _navigateToBuyerProductDetail

    fun onBuyerProductClicked(buyerProductResponse: BuyerProductResponse) {
        _navigateToBuyerProductDetail.value = Event(buyerProductResponse)
    }

    private val _historyClick= MutableLiveData<Event<SearchHistory>>()
    val historyClick: LiveData<Event<SearchHistory>>
        get() = _historyClick

    fun onHistoryClick(searchHistory: SearchHistory) {
        _historyClick.value = Event(searchHistory)
    }


    private val _history = MutableLiveData<List<SearchHistory>>()
    val history : LiveData<List<SearchHistory>> get() = _history


    init {
        getSearchHistory()
    }
    private fun getSearchHistory() {
        viewModelScope.launch {
            _history.value =searchRepository.getSearchHistory()
        }
    }



    private val _insert = MutableLiveData<Long>()
    val insert: LiveData<Long> = _insert


    fun insertHistory(history:SearchHistory){
        viewModelScope.launch(Dispatchers.IO) {
            searchRepository.addSearchHistory(history)
        }
    }

    fun deleteHistory(){
        viewModelScope.launch(Dispatchers.IO) { searchRepository.deleteAllHistory() }
    }
}