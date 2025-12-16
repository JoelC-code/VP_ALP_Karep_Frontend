package com.example.vp_alp_karep_frontend.service

import com.example.todolistapp.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.ApplicationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApplicationService {
    @GET("api/companies/applications")
    fun getApplications(
        @Header("Authorization") token: String
    ): Call<ApplicationResponse>

    @GET("api/companies/jobs/{jobId}/applications/")
    fun getApplicationsByJob(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int
    ): Call<ApplicationResponse>

    @PUT("api/companies/applications/:applicationId/accept")
    fun acceptApplication(
        @Header("Authorization") token: String,
        @Path("applicationId") applicationId: Int
    ): Call<GeneralResponseModel>

    @PUT("api/companies/applications/:applicationId/reject")
    fun rejectApplication(
        @Header("Authorization") token: String,
        @Path("applicationId") applicationId: Int
    ): Call<GeneralResponseModel>
}