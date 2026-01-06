package com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates

import com.example.vp_alp_karep_frontend.models.CompanyModels.JobCompanyModel

sealed interface JobDetailStatusUIState {
    data class Success(
        val job: JobCompanyModel
    ): JobDetailStatusUIState
    object SuccessNoData: JobDetailStatusUIState  // For create/update that don't return data
    object Loading: JobDetailStatusUIState
    object Start: JobDetailStatusUIState
    data class Failed(
        val errorMessage: String
    ): JobDetailStatusUIState
}

