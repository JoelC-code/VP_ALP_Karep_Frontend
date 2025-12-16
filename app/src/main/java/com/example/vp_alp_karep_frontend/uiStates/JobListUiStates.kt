package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.JobModel

sealed class JobListUiStates {
    object Start: JobListUiStates()
    object Loading: JobListUiStates()
    data class Success(val jobs: List<JobModel>) : JobListUiStates()
    data class Error(val message: String): JobListUiStates()
}