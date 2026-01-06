package com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates

import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyModel

sealed interface CompanyStatusUIState {
    data class Success(
        val companyModel: CompanyModel
    ): CompanyStatusUIState
    object Loading: CompanyStatusUIState
    object Start: CompanyStatusUIState
    data class Failed(
        val errorMessage: String
    ): CompanyStatusUIState
}