package com.example.vp_alp_karep_frontend.service

import com.example.todolistapp.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.AllNotificationsResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface NotificationService {
    @GET("api/users/notifications")
    fun getNotifications(
        @Header("Authorization") token: String
    ): Call<AllNotificationsResponse>

    @DELETE("api/users/notifications/{notificationId}")
    fun deleteNotification(
        @Header("Authorization") token: String,
        @Path("notificationId") notificationId: Int
    ): Call<GeneralResponseModel>
}