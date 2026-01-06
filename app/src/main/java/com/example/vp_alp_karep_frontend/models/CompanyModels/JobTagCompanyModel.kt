package com.example.vp_alp_karep_frontend.models.CompanyModels

data class JobTagCompanyModel (
    val id: Int = 0,
    val name: String = "",
)

data class JobTagResponse (
    val data: JobTagCompanyModel? = null
)

data class JobTagsResponse (
    val data: List<JobTagCompanyModel> = emptyList()
)

