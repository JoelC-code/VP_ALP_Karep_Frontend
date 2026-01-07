package com.example.vp_alp_karep_frontend.models

import com.google.gson.annotations.SerializedName

data class UpdateEmailResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("email")
    val email: String
)

