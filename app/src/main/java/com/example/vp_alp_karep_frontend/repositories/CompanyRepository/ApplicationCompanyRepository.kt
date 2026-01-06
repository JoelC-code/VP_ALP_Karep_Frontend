package com.example.vp_alp_karep_frontend.repositories.CompanyRepository

import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.ApplicationResponse
import com.example.vp_alp_karep_frontend.service.CompanyService.ApplicationService
import retrofit2.Call

interface ApplicationCompanyRepositoryInterface {
    fun getApplications(token: String): Call<ApplicationResponse>
    fun getApplicationsByJob(
        token: String,
        jobId: Int
    ): Call<ApplicationResponse>
    fun acceptApplication(
        token: String,
        applicationId: Int
    ): Call<GeneralResponseCompanyModel>
    fun rejectApplication(
        token: String,
        applicationId: Int
    ): Call<GeneralResponseCompanyModel>
}

class ApplicationCompanyRepository(
    private val applicationService: ApplicationService
): ApplicationCompanyRepositoryInterface {
    override fun getApplications(token: String): Call<ApplicationResponse> {
        return applicationService.getApplications("Bearer ${token}")
    }

    override fun getApplicationsByJob(
        token: String,
        jobId: Int
    ): Call<ApplicationResponse> {
        return applicationService.getApplicationsByJob("Bearer ${token}", jobId)
    }

    override fun acceptApplication(
        token: String,
        applicationId: Int
    ): Call<GeneralResponseCompanyModel> {
        return applicationService.acceptApplication("Bearer ${token}", applicationId)
    }

    override fun rejectApplication(
        token: String,
        applicationId: Int
    ): Call<GeneralResponseCompanyModel> {
        return applicationService.rejectApplication("Bearer ${token}", applicationId)
    }

}