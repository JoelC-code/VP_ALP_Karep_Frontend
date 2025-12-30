package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.JobModel

sealed interface JobStatusUIState {
    data class Success(
        val jobs: List<JobModel>
    ): JobStatusUIState
    object Loading: JobStatusUIState
    object Start: JobStatusUIState
    data class Failed(
        val errorMessage: String
    ): JobStatusUIState
}