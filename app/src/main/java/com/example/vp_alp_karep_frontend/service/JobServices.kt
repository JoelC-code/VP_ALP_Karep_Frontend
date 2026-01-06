package com.example.vp_alp_karep_frontend.service

import com.example.vp_alp_karep_frontend.models.JobListResponse
import com.example.vp_alp_karep_frontend.models.JobResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface JobServices {
    @GET("api/job-list")
    fun getAllJobs(
        @Header("Authorization") token: String
    ): Call<JobListResponse>

    @GET("api/job/{jobId}")
    fun getJob(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int
    ): Call<JobResponse>

    @GET("api/job-list/job")
    fun searchJobs(
        @Header("Authorization") token: String,
        @Query("search") search: String?
    ): Call<JobListResponse>

    @GET("api/job-list/company")
    fun jobByCompany(
        @Header("Authorization") token: String,
    ): Call<JobListResponse>
}