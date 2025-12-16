package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.NotificationModel

sealed interface NotificationStatusUIState {
    data class Success(
        val notifications: List<NotificationModel>
    ): NotificationStatusUIState
    object Loading: NotificationStatusUIState
    object Start: NotificationStatusUIState
    data class Failed(
        val errorMessage: String
    ): NotificationStatusUIState
}