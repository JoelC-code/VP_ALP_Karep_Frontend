package com.example.vp_alp_karep_frontend.service

import retrofit2.Call
import com.example.vp_alp_karep_frontend.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.GetAllApplicationResponse
import com.example.vp_alp_karep_frontend.models.GetApplicationResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApplicationServices {
    @GET("api/application-list")
    fun getMyApplications(
        @Header("Authorization") token: String
    ): Call<GetAllApplicationResponse>

    @POST("api/application/{jobId}")
    fun hiringApply(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int
    ): Call<GeneralResponseModel>

    @PATCH("api/application/{appId}")
    fun cancelApplication(
        @Header("Authorization") token: String,
        @Path("appId") appId: Int
    ): Call<GetApplicationResponse>

    @DELETE("api/application/{appId}")
    fun deleteApplication(
        @Header("Authorization") token: String,
        @Path("appId") appId: Int
    ): Call<GeneralResponseModel>
}