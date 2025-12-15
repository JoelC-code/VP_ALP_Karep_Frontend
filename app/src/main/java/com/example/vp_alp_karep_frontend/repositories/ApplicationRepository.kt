package com.example.vp_alp_karep_frontend.repositories

import com.example.vp_alp_karep_frontend.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.GetAllApplicationResponse
import com.example.vp_alp_karep_frontend.models.GetApplicationResponse
import com.example.vp_alp_karep_frontend.service.ApplicationServices
import retrofit2.Call

interface ApplicationRepositoryInterface {
    fun getMyApplications(token: String): Call<GetAllApplicationResponse>
    fun hiringApply(token: String, jobId: Int): Call<GeneralResponseModel>
    fun cancelApplication(token: String, appId: Int): Call<GetApplicationResponse>
    fun deleteApplication(token: String, appId: Int): Call<GeneralResponseModel>
}

class ApplicationRepository(
    private val applicationAPI: ApplicationServices
): ApplicationRepositoryInterface {
    override fun getMyApplications(token: String): Call<GetAllApplicationResponse> {
        return applicationAPI.getMyApplications("Bearer ${token}")
    }

    override fun hiringApply(token: String, jobId: Int): Call<GeneralResponseModel> {
        return applicationAPI.hiringApply("Bearer ${token}", jobId)
    }

    override fun cancelApplication(token: String, appId: Int): Call<GetApplicationResponse> {
        return applicationAPI.cancelApplication("Bearer ${token}", appId)
    }

    override fun deleteApplication(token: String, appId: Int): Call<GeneralResponseModel> {
        return applicationAPI.deleteApplication("Bearer ${token}", appId)
    }
}