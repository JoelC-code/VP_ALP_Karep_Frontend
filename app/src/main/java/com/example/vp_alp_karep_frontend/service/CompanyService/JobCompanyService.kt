package com.example.vp_alp_karep_frontend.service.CompanyService

import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobCreateUpdateRequest
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobListResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobTagsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JobCompanyService {
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
    ): Call<GeneralResponseCompanyModel>

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