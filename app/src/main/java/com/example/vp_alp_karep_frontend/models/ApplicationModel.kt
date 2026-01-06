package com.example.vp_alp_karep_frontend.models

import com.example.vp_alp_karep_frontend.enums.ApplicationStatus
import com.google.gson.annotations.SerializedName

data class ApplicationModel (
    val id: Int = 0,
    val status: ApplicationStatus = ApplicationStatus.Pending,
    val job: JobModel? = null,

    @SerializedName("user_id")
    val userId: Int = 0
)

data class GetAllApplicationResponse (
    val data: List<ApplicationModel> = emptyList()
)

data class GetApplicationResponse(
    val data: ApplicationModel
)