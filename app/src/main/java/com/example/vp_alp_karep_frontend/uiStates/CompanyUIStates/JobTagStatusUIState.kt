package com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates

import com.example.vp_alp_karep_frontend.models.CompanyModels.JobTagCompanyModel

sealed interface JobTagStatusUIState {
    data class Success(
        val jobTags: List<JobTagCompanyModel>
    ): JobTagStatusUIState
    object Loading: JobTagStatusUIState
    object Start: JobTagStatusUIState
    data class Failed(
        val errorMessage: String
    ): JobTagStatusUIState
}

