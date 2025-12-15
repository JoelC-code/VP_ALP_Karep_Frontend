package com.example.vp_alp_karep_frontend.repositories

import com.example.vp_alp_karep_frontend.models.GetAllJobsResponse
import com.example.vp_alp_karep_frontend.models.GetJobResponse
import com.example.vp_alp_karep_frontend.service.JobServices
import retrofit2.Call

interface JobRepositoryInterface {
    fun getAllJobs(token: String): Call<GetAllJobsResponse>
    fun getJob(token: String, jobId: Int): Call<GetJobResponse>
    fun searchJobs(token: String, search: String): Call<GetAllJobsResponse>
    fun jobByCompany(token: String): Call<GetAllJobsResponse>
}

class JobRepository(
    private val jobAPI: JobServices
): JobRepositoryInterface {
    override fun getAllJobs(token: String): Call<GetAllJobsResponse> {
        return jobAPI.getAllJobs("Bearer ${token}")
    }

    override fun getJob(token: String, jobId: Int): Call<GetJobResponse> {
        return jobAPI.getJob("Bearer ${token}" ,jobId)
    }

    override fun searchJobs(token: String, search: String): Call<GetAllJobsResponse> {
        return jobAPI.searchJobs("Bearer ${token}", search)
    }

    override fun jobByCompany(token: String): Call<GetAllJobsResponse> {
        return jobAPI.jobByCompany("Bearer ${token}")
    }

}