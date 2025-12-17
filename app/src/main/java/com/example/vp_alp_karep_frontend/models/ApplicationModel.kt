package com.example.vp_alp_karep_frontend.models

data class ApplicationResponse(
    val data: List<ApplicationModel> = emptyList()
)

data class ApplicationModel(
    val id: Int = 0,
    val status: String = "",
    val user_id: Int = 0,
    val job: JobModel? = null
)
