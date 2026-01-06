package com.example.vp_alp_karep_frontend.models

data class JobTagModel (
    val id: Int = 0,
    val name: String = "",
)

data class JobTagResponse (
    val data: JobTagModel? = null
)

data class JobTagsResponse (
    val data: List<JobTagModel> = emptyList()
)

