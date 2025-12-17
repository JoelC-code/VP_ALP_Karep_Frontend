package com.example.vp_alp_karep_frontend.repositories

import com.example.todolistapp.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.AllNotificationsResponse
import com.example.vp_alp_karep_frontend.service.NotificationService
import retrofit2.Call

interface NotificationRepositoryInterface {
    fun getAllNotifications(token: String): Call<AllNotificationsResponse>
    fun deleteNotification(token: String, notificationId: Int): Call<GeneralResponseModel>
}

class NotificationRepository(
    private val notificationService: NotificationService
): NotificationRepositoryInterface {
    override fun getAllNotifications(token: String): Call<AllNotificationsResponse> {
        return notificationService.getNotifications("Bearer ${token}")
    }

    override fun deleteNotification(
        token: String,
        notificationId: Int
    ): Call<GeneralResponseModel> {
        return notificationService.deleteNotification("Bearer ${token}", notificationId)
    }
}