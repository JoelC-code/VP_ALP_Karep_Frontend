package com.example.vp_alp_karep_frontend.models

import java.util.Date

data class AllNotificationsResponse (
    val data: List<NotificationModel> = emptyList()
)

data class NotificationModel (
    val id: Int = 0,
    val title: String = "",
    val subtitle: String = "",
    val date: Date = Date()
)