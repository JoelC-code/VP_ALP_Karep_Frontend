package com.example.vp_alp_karep_frontend.models

data class JobTagModel (
    val id: Int = 0,
    val name: String = ""
)

data class GetAllJobTagResponse(
    val data: List<JobTagModel>
)