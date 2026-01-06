package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.ApplicationModel

sealed interface ApplicationStatusUIState {
    data class Success(val applications: List<ApplicationModel>): ApplicationStatusUIState
    object Start: ApplicationStatusUIState
    object Loading: ApplicationStatusUIState
    data class Failed(val errorMessage: String): ApplicationStatusUIState
}