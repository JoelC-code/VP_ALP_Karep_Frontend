package com.example.vp_alp_karep_frontend.repositories

import com.example.vp_alp_karep_frontend.models.GetAllJobTagResponse
import com.example.vp_alp_karep_frontend.service.JobTagService
import retrofit2.Call

interface JobTagRepositoryInterface {
    fun getAllTags(token: String): Call<GetAllJobTagResponse>
}

class JobTagRepository(
    private val jobTagAPI: JobTagService
): JobTagRepositoryInterface {
    override fun getAllTags(token: String): Call<GetAllJobTagResponse> {
        return jobTagAPI.getAllTags("Bearer ${token}")
    }
}