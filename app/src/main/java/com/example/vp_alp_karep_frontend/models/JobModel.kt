package com.example.vp_alp_karep_frontend.models

import com.google.gson.annotations.SerializedName

data class JobModel (
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val tags: List<JobTagModel> = emptyList(),

    @SerializedName("company_id")
    val companyId: Int = 0,
)

data class GetAllJobsResponse(
    val data: List<JobModel>
)

data class GetJobResponse(
    val data: JobModel
)
