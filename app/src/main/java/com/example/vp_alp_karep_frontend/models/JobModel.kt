package com.example.vp_alp_karep_frontend.models

data class JobResponse (
    val data: JobModel?
)

data class JobListResponse (
    val data: List<JobModel> = emptyList()
)

data class JobModel (
    val id: Int = 0,
    val name: String = "",
    val description: String? = "",
    val company_id: Int = 0,
    val tags: List<JobTagResponse> = emptyList(),
)

data class JobCreateUpdateRequest (
    val name: String,
    val description: String?,
    val tags: List<Int> = emptyList(),
)



