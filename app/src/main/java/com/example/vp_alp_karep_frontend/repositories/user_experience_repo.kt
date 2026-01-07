package com.example.vp_alp_karep_frontend.repositories

import com.example.vp_alp_karep_frontend.models.*
import com.example.vp_alp_karep_frontend.service.UserExperienceService
import retrofit2.Response

interface UserExperienceRepositoryInterface {
    suspend fun createExperience(token: String, createExperienceRequest: CreateExperienceRequest): Response<ApiResponse<Experience>>
    suspend fun getAllExperiences(token: String): Response<ApiResponse<List<Experience>>>
    suspend fun getExperience(token: String, id: Int): Response<ApiResponse<Experience>>
    suspend fun updateExperience(token: String, id: Int, updateExperienceRequest: UpdateExperienceRequest): Response<ApiResponse<Experience>>
    suspend fun deleteExperience(token: String, id: Int): Response<SuccessResponse>
}

class UserExperienceRepository(
    private val userExperienceService: UserExperienceService
) : UserExperienceRepositoryInterface {

    override suspend fun createExperience(
        token: String,
        createExperienceRequest: CreateExperienceRequest
    ): Response<ApiResponse<Experience>> {
        return userExperienceService.createExperience("Bearer $token", createExperienceRequest)
    }

    override suspend fun getAllExperiences(token: String): Response<ApiResponse<List<Experience>>> {
        return userExperienceService.getAllExperiences("Bearer $token")
    }

    override suspend fun getExperience(token: String, id: Int): Response<ApiResponse<Experience>> {
        return userExperienceService.getExperience("Bearer $token", id)
    }

    override suspend fun updateExperience(
        token: String,
        id: Int,
        updateExperienceRequest: UpdateExperienceRequest
    ): Response<ApiResponse<Experience>> {
        return userExperienceService.updateExperience("Bearer $token", id, updateExperienceRequest)
    }

    override suspend fun deleteExperience(token: String, id: Int): Response<SuccessResponse> {
        return userExperienceService.deleteExperience("Bearer $token", id)
    }
}