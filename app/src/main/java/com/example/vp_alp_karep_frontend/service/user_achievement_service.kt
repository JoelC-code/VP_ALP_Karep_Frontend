package com.example.vp_alp_karep_frontend.service

import com.example.vp_alp_karep_frontend.models.*
import retrofit2.Response
import retrofit2.http.*

interface UserAchievementService {

    @POST("api/achievements")
    suspend fun createAchievement(
        @Header("Authorization") token: String,
        @Body createAchievementRequest: CreateAchievementRequest
    ): Response<ApiResponse<Achievement>>

    @GET("api/achievements")
    suspend fun getAllAchievements(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Achievement>>>

    @GET("api/achievements/{id}")
    suspend fun getAchievement(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ApiResponse<Achievement>>

    @PUT("api/achievements/{id}")
    suspend fun updateAchievement(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body updateAchievementRequest: UpdateAchievementRequest
    ): Response<ApiResponse<Achievement>>

    @DELETE("api/achievements/{id}")
    suspend fun deleteAchievement(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<SuccessResponse>
}