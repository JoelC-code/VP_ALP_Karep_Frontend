package com.example.vp_alp_karep_frontend.models

data class JobModelResponse (
    val data: JobModel
)

data class JobModel (
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val company_id: Int = 0,
    val tags: List<JobTagResponse> = emptyList(),
)


