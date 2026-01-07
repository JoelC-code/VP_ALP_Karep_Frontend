package com.example.vp_alp_karep_frontend.models

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("data")
    val data: T? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("errors")
    val errors: String? = null
)

data class ErrorResponse(
    @SerializedName("errors")
    val errors: String,

    @SerializedName("message")
    val message: String? = null
)

data class SuccessResponse(
    @SerializedName("message")
    val message: String
)
