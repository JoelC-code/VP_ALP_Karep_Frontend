package com.example.vp_alp_karep_frontend.models.CompanyModels

import java.util.Date

data class CompanyResponse (
    val data: CompanyModel
)

data class CompanyModel (
    val id: Int = 0,
    val email: String = "",
    val name: String = "",
    val address: String = "",
    val phone_number: String = "",
    val website: String = "",
    val vision_mission: String = "",
    val description: String = "",
    val founding_date: Date = Date(),
    val logo_path: String = "",
    val image_path: String = "",
    val user_id: Int = 0,
    val company_tags: List<CompanyTagsModel> = emptyList(),
    val total_jobs: Int = 0
)

data class CompanyUpdateRequest (
    val name: String,
    val address: String?,
    val phone_number: String?,
    val website: String?,
    val vision_mission: String?,
    val description: String?,
    val founding_date: Date?,
    val logo_path: String?,
    val image_path: String?
)