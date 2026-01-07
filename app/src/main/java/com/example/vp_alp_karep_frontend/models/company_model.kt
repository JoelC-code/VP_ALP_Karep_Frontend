package com.example.vp_alp_karep_frontend.models

import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("company_name")
    val companyName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("logo_url")
    val logoUrl: String? = null,

    @SerializedName("website")
    val website: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)

data class CompanyRegisterRequest(
    @SerializedName("company_name")
    val companyName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("website")
    val website: String? = null
)

data class CompanyRegisterResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("company")
    val company: Company
)
