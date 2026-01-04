package com.example.vp_alp_karep_frontend.service

import com.example.todolistapp.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.JobCreateUpdateRequest
import com.example.vp_alp_karep_frontend.models.JobListResponse
import com.example.vp_alp_karep_frontend.models.JobResponse
import com.example.vp_alp_karep_frontend.models.JobTagsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JobService {
    @GET("api/companies/jobs")
    fun getAllJobsByCompany(
        @Header("Authorization") token: String
    ): Call<JobListResponse>

    @GET("api/companies/jobs/{jobId}")
    fun getJob(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int
    ): Call<JobResponse>

    @DELETE("api/companies/jobs/{jobId}")
    fun deleteJob(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int
    ): Call<GeneralResponseModel>

    @POST("api/companies/jobs")
    fun createJob(
        @Header("Authorization") token: String,
        @Body jobModel: JobCreateUpdateRequest
    ): Call<JobResponse>

    @PUT("api/companies/jobs/{jobId}")
    fun updateJob(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int,
        @Body jobModel: JobCreateUpdateRequest
    ): Call<JobResponse>

    @GET("api/jobtag-list")
    fun  getAllJobTags(
        @Header("Authorization") token: String,
    ): Call<JobTagsResponse>
}