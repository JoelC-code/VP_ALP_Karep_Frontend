package com.example.vp_alp_karep_frontend.repositories

import com.example.vp_alp_karep_frontend.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.GetApplicationResponse
import com.example.vp_alp_karep_frontend.service.ApplicationService
import retrofit2.Call

interface ApplicationRepositoryInterface {
    fun getApplications(token: String): Call<GetApplicationResponse>
    fun getApplicationsByJob(
        token: String,
        jobId: Int
    ): Call<GetApplicationResponse>
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
    override fun getApplications(token: String): Call<GetApplicationResponse> {
        return applicationService.getApplications("Bearer ${token}")
    }

    override fun getApplicationsByJob(
        token: String,
        jobId: Int
    ): Call<GetApplicationResponse> {
        return applicationService.getApplicationsByJob("Bearer ${token}", jobId)
    }

    override fun acceptApplication(
        token: String,
        applicationId: Int
    ): Call<GeneralResponseModel> {
        return applicationService.acceptApplication("Bearer ${token}", applicationId)
    }

    override fun rejectApplication(
        token: String,
        applicationId: Int
    ): Call<GeneralResponseModel> {
        return applicationService.rejectApplication("Bearer ${token}", applicationId)
    }

}