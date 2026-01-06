package com.example.vp_alp_karep_frontend.models.CompanyModels

data class JobResponse (
    val data: JobCompanyModel?
)

data class JobListResponse (
    val data: List<JobCompanyModel> = emptyList()
)

data class JobCompanyModel (
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



