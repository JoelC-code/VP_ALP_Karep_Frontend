package com.example.vp_alp_karep_frontend.models

data class ApplicationResponse(
    val data: ApplicationModel
)

data class ApplicationModel(
    val id: Int = 0,
    val status: String = "",
    val user_id: Int = 0,
    val job: List<JobModelResponse> = emptyList()
)
