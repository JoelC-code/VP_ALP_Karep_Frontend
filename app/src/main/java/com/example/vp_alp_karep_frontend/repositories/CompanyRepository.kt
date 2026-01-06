package com.example.vp_alp_karep_frontend.repositories

import com.example.todolistapp.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.CompanyResponse
import com.example.vp_alp_karep_frontend.models.CompanyUpdateRequest
import com.example.vp_alp_karep_frontend.service.CompanyService
import retrofit2.Call
import java.util.Date

interface CompanyRepositoryInterface {
    fun getCompanyProfile(token: String): Call<CompanyResponse>
    fun updateCompanyProfile(
        token: String,
        name: String,
        address: String?,
        phone_number: String?,
        website: String?,
        vision_mission: String?,
        description: String?,
        founding_date: Date?,
        logo_path: String?,
        image_path: String?
    ): Call<GeneralResponseModel>
}

class CompanyRepository(
    private val companyService: CompanyService
): CompanyRepositoryInterface {
    override fun getCompanyProfile(token: String): Call<CompanyResponse> {
        return companyService.getCompanyProfile("Bearer ${token}")
    }

    override fun updateCompanyProfile(
        token: String,
        name: String,
        address: String?,
        phone_number: String?,
        website: String?,
        vision_mission: String?,
        description: String?,
        founding_date: Date?,
        logo_path: String?,
        image_path: String?
    ): Call<GeneralResponseModel> {
        return companyService.updateCompanyProfile(
            "Bearer ${token}",
            CompanyUpdateRequest(
                name,
                address,
                phone_number,
                website,
                vision_mission,
                description,
                founding_date,
                logo_path,
                image_path
            )
        )
    }

}