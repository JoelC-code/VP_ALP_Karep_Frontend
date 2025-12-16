package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.JobTagModel

sealed class JobTagUiStates {
    object Start: JobTagUiStates()
    object Loading: JobTagUiStates()
    data class Success(val tags: List<JobTagModel>): JobTagUiStates()
    data class Error(val message: String) : JobTagUiStates()
}