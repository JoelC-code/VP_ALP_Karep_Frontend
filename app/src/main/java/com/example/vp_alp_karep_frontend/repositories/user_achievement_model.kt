package com.example.vp_alp_karep_frontend.repositories

import com.example.vp_alp_karep_frontend.models.*
import com.example.vp_alp_karep_frontend.service.UserAchievementService
import retrofit2.Response

interface UserAchievementRepositoryInterface {
    suspend fun createAchievement(token: String, createAchievementRequest: CreateAchievementRequest): Response<ApiResponse<Achievement>>
    suspend fun getAllAchievements(token: String): Response<ApiResponse<List<Achievement>>>
    suspend fun getAchievement(token: String, id: Int): Response<ApiResponse<Achievement>>
    suspend fun updateAchievement(token: String, id: Int, updateAchievementRequest: UpdateAchievementRequest): Response<ApiResponse<Achievement>>
    suspend fun deleteAchievement(token: String, id: Int): Response<SuccessResponse>
}

class UserAchievementRepository(
    private val userAchievementService: UserAchievementService
) : UserAchievementRepositoryInterface {

    override suspend fun createAchievement(
        token: String,
        createAchievementRequest: CreateAchievementRequest
    ): Response<ApiResponse<Achievement>> {
        return userAchievementService.createAchievement("Bearer $token", createAchievementRequest)
    }

    override suspend fun getAllAchievements(token: String): Response<ApiResponse<List<Achievement>>> {
        return userAchievementService.getAllAchievements("Bearer $token")
    }

    override suspend fun getAchievement(token: String, id: Int): Response<ApiResponse<Achievement>> {
        return userAchievementService.getAchievement("Bearer $token", id)
    }

    override suspend fun updateAchievement(
        token: String,
        id: Int,
        updateAchievementRequest: UpdateAchievementRequest
    ): Response<ApiResponse<Achievement>> {
        return userAchievementService.updateAchievement("Bearer $token", id, updateAchievementRequest)
    }

    override suspend fun deleteAchievement(token: String, id: Int): Response<SuccessResponse> {
        return userAchievementService.deleteAchievement("Bearer $token", id)
    }
}