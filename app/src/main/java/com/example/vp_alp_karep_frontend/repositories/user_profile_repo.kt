package com.example.vp_alp_karep_frontend.repositories

import com.example.vp_alp_karep_frontend.models.*
import com.example.vp_alp_karep_frontend.service.UserProfileService
import retrofit2.Response

interface UserProfileRepositoryInterface {
    suspend fun getProfile(token: String): Response<UserProfileResponse>
    suspend fun updateProfile(token: String, updateProfileRequest: UpdateProfileRequest): Response<UserProfileResponse>
    suspend fun updateEmail(token: String, updateEmailRequest: UpdateEmailRequest): Response<UpdateEmailWrapperResponse>
    suspend fun updatePassword(token: String, updatePasswordRequest: UpdatePasswordRequest): Response<UpdatePasswordWrapperResponse>
}

class UserProfileRepository(
    private val userProfileService: UserProfileService
) : UserProfileRepositoryInterface {

    override suspend fun getProfile(token: String): Response<UserProfileResponse> {
        return userProfileService.getProfile("Bearer $token")
    }

    override suspend fun updateProfile(
        token: String,
        updateProfileRequest: UpdateProfileRequest
    ): Response<UserProfileResponse> {
        return userProfileService.updateProfile("Bearer $token", updateProfileRequest)
    }

    override suspend fun updateEmail(
        token: String,
        updateEmailRequest: UpdateEmailRequest
    ): Response<UpdateEmailWrapperResponse> {
        return userProfileService.updateEmail("Bearer $token", updateEmailRequest)
    }

    override suspend fun updatePassword(
        token: String,
        updatePasswordRequest: UpdatePasswordRequest
    ): Response<UpdatePasswordWrapperResponse> {
        return userProfileService.updatePassword("Bearer $token", updatePasswordRequest)
    }
}