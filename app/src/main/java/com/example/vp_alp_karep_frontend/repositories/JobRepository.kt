package com.example.vp_alp_karep_frontend.repositories

import com.example.todolistapp.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.JobCreateUpdateRequest
import com.example.vp_alp_karep_frontend.models.JobListResponse
import com.example.vp_alp_karep_frontend.models.JobResponse
import com.example.vp_alp_karep_frontend.models.JobTagsResponse
import com.example.vp_alp_karep_frontend.service.JobService
import retrofit2.Call

interface JobRepositoryInterface {
    fun getAllJobsByCompany(token: String): Call<JobListResponse>
    fun getJob(
        token: String,
        jobId: Int
    ): Call<JobResponse>
    fun deleteJob(
        token: String,
        jobId: Int
    ): Call<GeneralResponseModel>
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
    fun getAllJobTags(): Call<JobTagsResponse>
}

class JobRepository(
    private val jobService: JobService
): JobRepositoryInterface {
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

    override fun deleteJob(token: String, jobId: Int): Call<GeneralResponseModel> {
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

    override fun getAllJobTags(): Call<JobTagsResponse> {
        return jobService.getAllJobTags()
    }
}