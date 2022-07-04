package com.binar.secondhand.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.NotificatioRepository
import com.binar.secondhand.data.source.remote.response.NotificationResponseItem

class NotificationViewModel(
    private val notificatioRepository: NotificatioRepository,
): ViewModel() {

    private val _getNotificationID = MutableLiveData<Int>()
    val patchNotification: LiveData<Result<NotificationResponseItem>> = _getNotificationID.switchMap {
        notificatioRepository.patchNotification(it)
    }

    fun getAllNotification(): LiveData<Result<List<NotificationResponseItem>>>{
        return notificatioRepository.getAllNotification()
    }

    fun getUnreadCount (): LiveData<Result<Int>>{
        return notificatioRepository.getUnreadCount()
    }

    fun doPatchNotification(id: Int){
        _getNotificationID.value = id
    }

}