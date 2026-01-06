package com.example.vp_alp_karep_frontend.repositories.CompanyRepository

import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyTagCreateRequest
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyTagsResponse
import com.example.vp_alp_karep_frontend.service.CompanyService.CompanyTagService
import retrofit2.Call

interface CompanyTagRepositoryInterface {
    fun createCompanyToTags(
        token: String,
        company_id: Int,
        tag_id: Int
    ): Call<GeneralResponseCompanyModel>
    fun deleteCompanyToTags(
        token: String,
        tagId: Int
    ): Call<GeneralResponseCompanyModel>
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
    ): Call<GeneralResponseCompanyModel> {
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
    ): Call<GeneralResponseCompanyModel> {
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