package com.binar.secondhand.ui.account.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.HistoryRepository
import com.binar.secondhand.data.source.remote.response.HistoryResponseItem


class HistoryViewModel(
    private val historyRepository: HistoryRepository
):ViewModel(){
    fun getAllHistory(): LiveData<Result<List<HistoryResponseItem>>> {
        return historyRepository.history()
    }
}