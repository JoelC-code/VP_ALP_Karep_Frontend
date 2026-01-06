package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.JobModel

sealed interface JobDetailStatusUIState {
    data class Success(
        val job: JobModel
    ): JobDetailStatusUIState
    object SuccessNoData: JobDetailStatusUIState  // For create/update that don't return data
    object Loading: JobDetailStatusUIState
    object Start: JobDetailStatusUIState
    data class Failed(
        val errorMessage: String
    ): JobDetailStatusUIState
}

