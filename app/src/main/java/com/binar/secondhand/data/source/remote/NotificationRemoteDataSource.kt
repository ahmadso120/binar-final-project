package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.NotificationService

class NotificationRemoteDataSource(private val notificationService: NotificationService) {

    suspend fun getAllNotification()= notificationService.getAllNotification()

    suspend fun patchNotification(id : Int)= notificationService.patchNotification(id)
}