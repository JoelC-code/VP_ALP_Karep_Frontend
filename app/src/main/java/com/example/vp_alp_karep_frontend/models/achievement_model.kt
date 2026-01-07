package com.example.vp_alp_karep_frontend.models

import com.google.gson.annotations.SerializedName

data class Achievement(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("user_id")
    val userId: Int? = null,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,
)

data class CreateAchievementRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,
)

data class UpdateAchievementRequest(
    @SerializedName("title")
    val title: String? = null,

    @SerializedName("description")
    val description: String? = null,
)
