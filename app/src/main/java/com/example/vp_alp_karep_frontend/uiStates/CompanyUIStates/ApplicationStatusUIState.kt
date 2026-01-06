package com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates

import com.example.vp_alp_karep_frontend.models.CompanyModels.ApplicationCompanyModel

sealed interface ApplicationStatusUIState {
    data class Success(val applications: List<ApplicationCompanyModel>): ApplicationStatusUIState
    object Start: ApplicationStatusUIState
    object Loading: ApplicationStatusUIState
    data class Failed(val errorMessage: String): ApplicationStatusUIState
}