package com.example.vp_alp_karep_frontend.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("name")
    val fullName: String? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
) {
    // Computed property untuk backward compatibility
    val username: String get() = fullName ?: email.substringBefore("@")
}

data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("data")
    val data: LoginData
)

data class LoginData(
    @SerializedName("token")
    val token: String,

    @SerializedName("account_type")
    val accountType: String
)

data class RegisterRequest(
    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null
)

data class RegisterResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("user")
    val user: User
)

data class UpdateProfileRequest(
    @SerializedName("name")
    val fullName: String? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("address")
    val address: String? = null
)

// Wrapper responses to avoid nested generic issues with Retrofit
data class UserProfileResponse(
    @SerializedName("data")
    val data: User? = null
)

data class UpdateEmailWrapperResponse(
    @SerializedName("data")
    val data: UpdateEmailResponse? = null
)

data class UpdatePasswordWrapperResponse(
    @SerializedName("data")
    val data: UpdatePasswordResponse? = null
)

