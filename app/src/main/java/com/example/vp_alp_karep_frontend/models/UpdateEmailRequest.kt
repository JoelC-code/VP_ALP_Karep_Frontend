package com.example.vp_alp_karep_frontend.models

import com.google.gson.annotations.SerializedName

data class UpdateEmailRequest(
    @SerializedName("current_password")
    val currentPassword: String,

    @SerializedName("new_email")
    val newEmail: String
)
