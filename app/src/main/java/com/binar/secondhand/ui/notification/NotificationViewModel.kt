package com.binar.secondhand.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.NotificatioRepository
import com.binar.secondhand.data.source.remote.response.NotificationResponseItem

class NotificationViewModel(private val notificatioRepository: NotificatioRepository
): ViewModel() {

    fun getAllNotification(): LiveData<Result<List<NotificationResponseItem>>>{
        return notificatioRepository.getAllNotification()
    }
}