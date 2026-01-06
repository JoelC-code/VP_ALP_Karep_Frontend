package com.example.vp_alp_karep_frontend.models

data class CompanyTagsResponse (
    val data: List<CompanyTagsModel> = emptyList()
)

data class CompanyTagsModel (
    val id: Int = 0,
    val name: String = "",
)

data class CompanyTagCreateRequest (
    val company_id: Int = 0,
    val tag_id: Int = 0,
)