package com.example.vp_alp_karep_frontend.service

import com.example.vp_alp_karep_frontend.models.GetAllJobTagResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface JobTagService {
    @GET("api/jobtag-list")
    fun getAllTags(
        @Header("Authorization") token: String
    ): Call<GetAllJobTagResponse>
}