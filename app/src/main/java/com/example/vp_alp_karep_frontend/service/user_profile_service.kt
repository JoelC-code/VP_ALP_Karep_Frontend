package com.example.vp_alp_karep_frontend.service

import com.example.vp_alp_karep_frontend.models.*
import com.example.vp_alp_karep_frontend.models.UpdateEmailRequest
import com.example.vp_alp_karep_frontend.models.UpdateEmailResponse
import com.example.vp_alp_karep_frontend.models.UpdatePasswordRequest
import com.example.vp_alp_karep_frontend.models.UpdatePasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface UserProfileService {
    
    @GET("api/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserProfileResponse>

    @PUT("api/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): Response<UserProfileResponse>

    @PUT("api/profile/email")
    suspend fun updateEmail(
        @Header("Authorization") token: String,
        @Body updateEmailRequest: UpdateEmailRequest
    ): Response<UpdateEmailWrapperResponse>

    @PUT("api/profile/password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body updatePasswordRequest: UpdatePasswordRequest
    ): Response<UpdatePasswordWrapperResponse>
}