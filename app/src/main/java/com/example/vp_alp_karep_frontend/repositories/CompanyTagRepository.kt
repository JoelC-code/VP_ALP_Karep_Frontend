package com.example.vp_alp_karep_frontend.repositories

import com.example.vp_alp_karep_frontend.models.CompanyTagCreateRequest
import com.example.vp_alp_karep_frontend.models.CompanyTagsResponse
import com.example.vp_alp_karep_frontend.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.service.CompanyTagService
import retrofit2.Call

interface CompanyTagRepositoryInterface {
    fun createCompanyToTags(
        token: String,
        company_id: Int,
        tag_id: Int
    ): Call<GeneralResponseModel>
    fun deleteCompanyToTags(
        token: String,
        tagId: Int
    ): Call<GeneralResponseModel>
    fun getAllCompanyTags(
        token: String
    ): Call<CompanyTagsResponse>
}

class CompanyTagRepository(
    private val companyTagService: CompanyTagService
): CompanyTagRepositoryInterface {
    override fun createCompanyToTags(
        token: String,
        company_id: Int,
        tag_id: Int
    ): Call<GeneralResponseModel> {
        val companyTagCreateRequest = CompanyTagCreateRequest(
            company_id = company_id,
            tag_id = tag_id
        )
        return companyTagService.createCompanyToTags(
            "Bearer ${token}",
            companyTagCreateRequest
        )
    }

    override fun deleteCompanyToTags(
        token: String,
        tagId: Int
    ): Call<GeneralResponseModel> {
        return companyTagService.deleteCompanyToTags(
            "Bearer ${token}",
            tagId
        )
    }

    override fun getAllCompanyTags(
        token: String
    ): Call<CompanyTagsResponse> {
        return companyTagService.getAllCompanyTags(
            "Bearer ${token}"
        )
    }

}