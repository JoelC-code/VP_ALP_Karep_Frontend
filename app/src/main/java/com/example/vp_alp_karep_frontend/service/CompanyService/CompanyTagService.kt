package com.example.vp_alp_karep_frontend.service.CompanyService

import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyTagCreateRequest
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyTagsResponse
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
    ): Call<GeneralResponseCompanyModel>

    @DELETE("api/companies/profile/tags/{tagId}")
    fun deleteCompanyToTags(
        @Header("Authorization") token: String,
        @Path("tagId") tagId: Int
    ): Call<GeneralResponseCompanyModel>

    @GET("api/company-tags")
    fun getAllCompanyTags(
        @Header("Authorization") token: String,
    ): Call<CompanyTagsResponse>
}