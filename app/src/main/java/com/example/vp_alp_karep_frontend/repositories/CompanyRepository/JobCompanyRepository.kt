package com.example.vp_alp_karep_frontend.repositories.CompanyRepository

import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobCreateUpdateRequest
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobListResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobTagsResponse
import com.example.vp_alp_karep_frontend.service.CompanyService.JobCompanyService
import retrofit2.Call

interface JobCompanyRepositoryInterface {
    fun getAllJobsByCompany(token: String): Call<JobListResponse>
    fun getJob(
        token: String,
        jobId: Int
    ): Call<JobResponse>
    fun deleteJob(
        token: String,
        jobId: Int
    ): Call<GeneralResponseCompanyModel>
    fun createJob(
        token: String,
        name: String,
        description: String?,
        tags: List<Int> = emptyList()
    ): Call<JobResponse>
    fun updateJob(
        token: String,
        jobId: Int,
        name: String,
        description: String?,
        tags: List<Int> = emptyList()
    ): Call<JobResponse>
    fun getAllJobTags(
        token: String
    ): Call<JobTagsResponse>
}

class JobCompanyRepository(
    private val jobService: JobCompanyService
): JobCompanyRepositoryInterface {
    override fun getAllJobsByCompany(token: String): Call<JobListResponse> {
        return jobService.getAllJobsByCompany("Bearer ${token}")
    }

    override fun getJob(
        token: String,
        jobId: Int
    ): Call<JobResponse> {
        return jobService.getJob(
            "Bearer ${token}",
            jobId
        )
    }

    override fun deleteJob(token: String, jobId: Int): Call<GeneralResponseCompanyModel> {
        return jobService.deleteJob(
            "Bearer ${token}",
            jobId
        )
    }

    override fun createJob(
        token: String,
        name: String,
        description: String?,
        tags: List<Int>
    ): Call<JobResponse> {
        return jobService.createJob(
            "Bearer ${token}",
            JobCreateUpdateRequest(
                name,
                description,
                tags
            )
        )
    }

    override fun updateJob(
        token: String,
        jobId: Int,
        name: String,
        description: String?,
        tags: List<Int>
    ): Call<JobResponse> {
        return jobService.updateJob(
            "Bearer ${token}",
            jobId,
            JobCreateUpdateRequest(
                name,
                description,
                tags
            )
        )
    }

    override fun getAllJobTags(
        token: String
    ): Call<JobTagsResponse> {
        return jobService.getAllJobTags(
            "Bearer ${token}",
        )
    }
}