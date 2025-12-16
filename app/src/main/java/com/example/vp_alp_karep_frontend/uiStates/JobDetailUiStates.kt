package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.JobModel

sealed class JobDetailUiStates {
    object start: JobDetailUiStates()
    object Loading: JobDetailUiStates()
    data class Success(val job: JobModel): JobDetailUiStates()
    data class Error(val message: String): JobDetailUiStates()
}