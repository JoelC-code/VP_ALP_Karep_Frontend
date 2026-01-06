package com.example.vp_alp_karep_frontend.service

import com.example.todolistapp.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.CompanyTagCreateRequest
import com.example.vp_alp_karep_frontend.models.CompanyTagsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CompanyTagService {
    @POST("api/companies/profile/tags")
    fun createCompanyToTags(
        @Header("Authorization") token: String,
        @Body companyTagsModel: CompanyTagCreateRequest
    ): Call<GeneralResponseModel>

    @DELETE("api/companies/profile/tags/{tagId}")
    fun deleteCompanyToTags(
        @Header("Authorization") token: String,
        @Path("tagId") tagId: Int
    ): Call<GeneralResponseModel>

    @GET("api/company-tags")
    fun getAllCompanyTags(
        @Header("Authorization") token: String,
    ): Call<CompanyTagsResponse>
}