package com.example.vp_alp_karep_frontend.repositories.CompanyRepository

import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.AllNotificationsResponse
import com.example.vp_alp_karep_frontend.service.CompanyService.NotificationService
import retrofit2.Call

interface NotificationRepositoryInterface {
    fun getAllNotifications(token: String): Call<AllNotificationsResponse>
    fun deleteNotification(token: String, notificationId: Int): Call<GeneralResponseCompanyModel>
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
    ): Call<GeneralResponseCompanyModel> {
        return notificationService.deleteNotification("Bearer ${token}", notificationId)
    }
}