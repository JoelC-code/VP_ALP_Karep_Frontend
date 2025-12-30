package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.JobTagModel

sealed interface JobTagStatusUIState {
    data class Success(
        val jobTags: List<JobTagModel>
    ): JobTagStatusUIState
    object Loading: JobTagStatusUIState
    object Start: JobTagStatusUIState
    data class Failed(
        val errorMessage: String
    ): JobTagStatusUIState
}

