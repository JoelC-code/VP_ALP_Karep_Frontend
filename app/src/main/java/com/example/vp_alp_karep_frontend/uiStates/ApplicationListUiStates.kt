package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.ApplicationModel

sealed class ApplicationListUiStates {
    object Start: ApplicationListUiStates()
    object Loading: ApplicationListUiStates()
    data class Success(val applications: List<ApplicationModel>) : ApplicationListUiStates()
    data class Error(val message: String): ApplicationListUiStates()
}