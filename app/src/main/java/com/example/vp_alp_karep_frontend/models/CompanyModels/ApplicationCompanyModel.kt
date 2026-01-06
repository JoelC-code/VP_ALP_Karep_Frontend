package com.example.vp_alp_karep_frontend.models.CompanyModels

data class ApplicationResponse(
    val data: List<ApplicationCompanyModel> = emptyList()
)

data class ApplicationCompanyModel(
    val id: Int = 0,
    val status: String = "",
    val user_id: Int = 0,
    val job: JobCompanyModel? = null
)
