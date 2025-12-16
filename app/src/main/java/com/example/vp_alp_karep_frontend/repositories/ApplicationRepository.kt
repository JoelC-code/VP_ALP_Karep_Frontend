package com.example.vp_alp_karep_frontend.repositories

import com.example.todolistapp.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.ApplicationResponse
import com.example.vp_alp_karep_frontend.service.ApplicationService
import retrofit2.Call

interface ApplicationRepositoryInterface {
    fun getApplications(token: String): Call<ApplicationResponse>
    fun getApplicationsByJob(
        token: String,
        jobId: Int
    ): Call<ApplicationResponse>
    fun acceptApplication(
        token: String,
        applicationId: Int
    ): Call<GeneralResponseModel>
    fun rejectApplication(
        token: String,
        applicationId: Int
    ): Call<GeneralResponseModel>
}

class ApplicationRepository(
    private val applicationService: ApplicationService
): ApplicationRepositoryInterface {
    override fun getApplications(token: String): Call<ApplicationResponse> {
        return applicationService.getApplications(token)
    }

    override fun getApplicationsByJob(
        token: String,
        jobId: Int
    ): Call<ApplicationResponse> {
        return applicationService.getApplicationsByJob(token, jobId)
    }

    override fun acceptApplication(
        token: String,
        applicationId: Int
    ): Call<GeneralResponseModel> {
        return applicationService.acceptApplication(token, applicationId)
    }

    override fun rejectApplication(
        token: String,
        applicationId: Int
    ): Call<GeneralResponseModel> {
        return applicationService.rejectApplication(token, applicationId)
    }

}