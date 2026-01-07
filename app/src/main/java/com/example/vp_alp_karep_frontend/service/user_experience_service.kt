package com.example.vp_alp_karep_frontend.service

import com.example.vp_alp_karep_frontend.models.*
import retrofit2.Response
import retrofit2.http.*

interface UserExperienceService {

    @POST("api/experiences")
    suspend fun createExperience(
        @Header("Authorization") token: String,
        @Body createExperienceRequest: CreateExperienceRequest
    ): Response<ApiResponse<Experience>>

    @GET("api/experiences")
    suspend fun getAllExperiences(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Experience>>>

    @GET("api/experiences/{id}")
    suspend fun getExperience(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ApiResponse<Experience>>

    @PUT("api/experiences/{id}")
    suspend fun updateExperience(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body updateExperienceRequest: UpdateExperienceRequest
    ): Response<ApiResponse<Experience>>

    @DELETE("api/experiences/{id}")
    suspend fun deleteExperience(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<SuccessResponse>
}