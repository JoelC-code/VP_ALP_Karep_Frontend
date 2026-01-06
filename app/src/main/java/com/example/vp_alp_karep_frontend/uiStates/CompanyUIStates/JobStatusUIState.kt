package com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates

import com.example.vp_alp_karep_frontend.models.CompanyModels.JobCompanyModel

sealed interface JobStatusUIState {
    data class Success(
        val jobs: List<JobCompanyModel>
    ): JobStatusUIState
    object Loading: JobStatusUIState
    object Start: JobStatusUIState
    data class Failed(
        val errorMessage: String
    ): JobStatusUIState
}