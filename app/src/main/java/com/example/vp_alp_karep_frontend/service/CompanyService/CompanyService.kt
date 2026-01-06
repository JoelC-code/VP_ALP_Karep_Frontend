package com.example.vp_alp_karep_frontend.service.CompanyService

import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyUpdateRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface CompanyService {
    @GET("api/companies/profile")
    fun getCompanyProfile(
        @Header("Authorization") token: String
    ): Call<CompanyResponse>

    @PUT("api/companies/profile")
    fun updateCompanyProfile(
        @Header("Authorization") token: String,
        @Body companyModel: CompanyUpdateRequest
    ): Call<GeneralResponseCompanyModel>

}