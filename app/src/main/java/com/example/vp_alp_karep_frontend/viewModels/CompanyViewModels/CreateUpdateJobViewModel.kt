package com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.CompanyModels.ErrorModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobTagsResponse
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.JobRepositoryInterface
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.JobTagStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.JobDetailStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateUpdateJobViewModel(
    private val jobRepository: JobRepositoryInterface
): ViewModel() {
    var getAllJobTagsStatus: JobTagStatusUIState by mutableStateOf(
        JobTagStatusUIState.Start
    )
        private set

    var getJobStatus: JobDetailStatusUIState by mutableStateOf(
        JobDetailStatusUIState.Start
    )
        private set

    var createJobStatus: JobDetailStatusUIState by mutableStateOf(
        JobDetailStatusUIState.Start
    )
        private set

    var updateJobStatus: JobDetailStatusUIState by mutableStateOf(
        JobDetailStatusUIState.Start
    )
        private set

    // Mode: "create" or "update"
    var mode: String by mutableStateOf("create")
        private set

    var currentJobId: Int? by mutableStateOf(null)
        private set

    var selectedTagIds by mutableStateOf<Set<Int>>(emptySet())
        private set

    fun setMode(newMode: String, jobId: Int? = null) {
        mode = newMode
        currentJobId = jobId
        // Clear all statuses when mode changes
        clearAllStatuses()
        // Clear selected tags
        selectedTagIds = emptySet()
    }

    fun toggleTagSelection(tagId: Int) {
        selectedTagIds = if (selectedTagIds.contains(tagId)) {
            selectedTagIds - tagId
        } else {
            selectedTagIds + tagId
        }
    }

    fun setSelectedTags(tagIds: Set<Int>) {
        selectedTagIds = tagIds
    }

    fun clearAllStatuses() {
        createJobStatus = JobDetailStatusUIState.Start
        updateJobStatus = JobDetailStatusUIState.Start
        getJobStatus = JobDetailStatusUIState.Start
    }

    fun getAllJobTags(token: String) {
        viewModelScope.launch {
            getAllJobTagsStatus = JobTagStatusUIState.Loading

            try {
                val call = jobRepository.getAllJobTags(token)

                call.enqueue(object: Callback<JobTagsResponse> {
                    override fun onResponse(
                        call: Call<JobTagsResponse>,
                        res: Response<JobTagsResponse>
                    ) {
                        if (res.isSuccessful) {
                            getAllJobTagsStatus = JobTagStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            getAllJobTagsStatus = JobTagStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<JobTagsResponse>,
                        t: Throwable
                    ) {
                        getAllJobTagsStatus = JobTagStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                getAllJobTagsStatus = JobTagStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun getJob(token: String, jobId: Int) {
        viewModelScope.launch {
            getJobStatus = JobDetailStatusUIState.Loading

            try {
                val call = jobRepository.getJob(token, jobId)

                call.enqueue(object: Callback<JobResponse> {
                    override fun onResponse(
                        call: Call<JobResponse>,
                        res: Response<JobResponse>
                    ) {
                        if (res.isSuccessful) {
                            val job = res.body()?.data
                            if (job != null) {
                                // Set selected tags from job data
                                selectedTagIds = job.tags.mapNotNull { tagResponse -> tagResponse.data?.id }.toSet()
                                getJobStatus = JobDetailStatusUIState.Success(job)
                            } else {
                                getJobStatus = JobDetailStatusUIState.Failed("Job data is null")
                            }
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            getJobStatus = JobDetailStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<JobResponse>,
                        t: Throwable
                    ) {
                        getJobStatus = JobDetailStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                getJobStatus = JobDetailStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun createJob(
        token: String,
        name: String,
        description: String?,
        tags: List<Int>
    ) {
        viewModelScope.launch {
            createJobStatus = JobDetailStatusUIState.Loading

            try {
                val call = jobRepository.createJob(token, name, description, tags)

                call.enqueue(object: Callback<JobResponse> {
                    override fun onResponse(
                        call: Call<JobResponse>,
                        res: Response<JobResponse>
                    ) {
                        if (res.isSuccessful) {
                            val job = res.body()?.data
                            if (job != null) {
                                createJobStatus = JobDetailStatusUIState.Success(job)
                            } else {
                                // Backend doesn't return data, but operation succeeded
                                createJobStatus = JobDetailStatusUIState.SuccessNoData
                            }
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            createJobStatus = JobDetailStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<JobResponse>,
                        t: Throwable
                    ) {
                        createJobStatus = JobDetailStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                createJobStatus = JobDetailStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun updateJob(
        token: String,
        jobId: Int,
        name: String,
        description: String?,
        tags: List<Int>
    ) {
        viewModelScope.launch {
            updateJobStatus = JobDetailStatusUIState.Loading

            try {
                val call = jobRepository.updateJob(token, jobId, name, description, tags)

                call.enqueue(object: Callback<JobResponse> {
                    override fun onResponse(
                        call: Call<JobResponse>,
                        res: Response<JobResponse>
                    ) {
                        if (res.isSuccessful) {
                            val job = res.body()?.data
                            if (job != null) {
                                updateJobStatus = JobDetailStatusUIState.Success(job)
                            } else {
                                // Backend doesn't return data, but operation succeeded
                                updateJobStatus = JobDetailStatusUIState.SuccessNoData
                            }
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            updateJobStatus = JobDetailStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<JobResponse>,
                        t: Throwable
                    ) {
                        updateJobStatus = JobDetailStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                updateJobStatus = JobDetailStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun clearCreateJobStatus() {
        createJobStatus = JobDetailStatusUIState.Start
    }

    fun clearUpdateJobStatus() {
        updateJobStatus = JobDetailStatusUIState.Start
    }

    fun clearGetJobStatus() {
        getJobStatus = JobDetailStatusUIState.Start
    }

    fun clearGetAllJobTagsStatus() {
        getAllJobTagsStatus = JobTagStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val jobRepository = application.container.jobRepository
                CreateUpdateJobViewModel(
                    jobRepository = jobRepository
                )
            }
        }
    }
}