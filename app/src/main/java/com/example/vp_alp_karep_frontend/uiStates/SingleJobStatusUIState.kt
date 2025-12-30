package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.JobModel

sealed interface SingleJobStatusUIState {
    data class Success(
        val job: JobModel
    ): SingleJobStatusUIState
    object Loading: SingleJobStatusUIState
    object Start: SingleJobStatusUIState
    data class Failed(
        val errorMessage: String
    ): SingleJobStatusUIState
}

