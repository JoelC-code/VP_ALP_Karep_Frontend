package com.example.vp_alp_karep_frontend.models

data class CompanyTagsResponse (
    val data: CompanyTagsModel
)

data class CompanyTagsModel (
    val id: Int = 0,
    val name: String = "",
)