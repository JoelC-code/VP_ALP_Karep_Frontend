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
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.JobCompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.JobTagStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.JobDetailStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateUpdateJobViewModel(
    private val jobRepository: JobCompanyRepositoryInterface,
    private val loginRepository: LoginRegistRepository
): ViewModel() {
    private val _getAllJobTagsStatus = MutableStateFlow<JobTagStatusUIState>(
        JobTagStatusUIState.Start
    )
    val getAllJobTagsStatus = _getAllJobTagsStatus

    private val _getJobStatus = MutableStateFlow<JobDetailStatusUIState>(
        JobDetailStatusUIState.Start
    )
    val getJobStatus = _getJobStatus

    private val _createJobStatus = MutableStateFlow<JobDetailStatusUIState>(
        JobDetailStatusUIState.Start
    )
    val createJobStatus = _createJobStatus

    private val _updateJobStatus = MutableStateFlow<JobDetailStatusUIState>(
        JobDetailStatusUIState.Start
    )
    val updateJobStatus = _updateJobStatus

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
        _createJobStatus.value = JobDetailStatusUIState.Start
        _updateJobStatus.value = JobDetailStatusUIState.Start
        _getJobStatus.value = JobDetailStatusUIState.Start
    }

    fun getAllJobTags() {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _getAllJobTagsStatus.value =
                    JobTagStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _getAllJobTagsStatus.value = JobTagStatusUIState.Loading

            try {
                val call = jobRepository.getAllJobTags(token)

                call.enqueue(object: Callback<JobTagsResponse> {
                    override fun onResponse(
                        call: Call<JobTagsResponse>,
                        res: Response<JobTagsResponse>
                    ) {
                        if (res.isSuccessful) {
                            _getAllJobTagsStatus.value = JobTagStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            _getAllJobTagsStatus.value = JobTagStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<JobTagsResponse>,
                        t: Throwable
                    ) {
                        _getAllJobTagsStatus.value = JobTagStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _getAllJobTagsStatus.value = JobTagStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun getJob(jobId: Int) {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _getJobStatus.value =
                    JobDetailStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _getJobStatus.value = JobDetailStatusUIState.Loading

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
                                _getJobStatus.value = JobDetailStatusUIState.Success(job)
                            } else {
                                _getJobStatus.value = JobDetailStatusUIState.Failed("Job data is null")
                            }
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            _getJobStatus.value = JobDetailStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<JobResponse>,
                        t: Throwable
                    ) {
                        _getJobStatus.value = JobDetailStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _getJobStatus.value = JobDetailStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun createJob(
        name: String,
        description: String?,
        tags: List<Int>
    ) {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _createJobStatus.value =
                    JobDetailStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _createJobStatus.value = JobDetailStatusUIState.Loading

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
                                _createJobStatus.value = JobDetailStatusUIState.Success(job)
                            } else {
                                // Backend doesn't return data, but operation succeeded
                                _createJobStatus.value = JobDetailStatusUIState.SuccessNoData
                            }
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            _createJobStatus.value = JobDetailStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<JobResponse>,
                        t: Throwable
                    ) {
                        _createJobStatus.value = JobDetailStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _createJobStatus.value = JobDetailStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun updateJob(
        jobId: Int,
        name: String,
        description: String?,
        tags: List<Int>
    ) {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _updateJobStatus.value =
                    JobDetailStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _updateJobStatus.value = JobDetailStatusUIState.Loading

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
                                _updateJobStatus.value = JobDetailStatusUIState.Success(job)
                            } else {
                                // Backend doesn't return data, but operation succeeded
                                _updateJobStatus.value = JobDetailStatusUIState.SuccessNoData
                            }
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            _updateJobStatus.value = JobDetailStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<JobResponse>,
                        t: Throwable
                    ) {
                        _updateJobStatus.value = JobDetailStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _updateJobStatus.value = JobDetailStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun clearCreateJobStatus() {
        _createJobStatus.value = JobDetailStatusUIState.Start
    }

    fun clearUpdateJobStatus() {
        _updateJobStatus.value = JobDetailStatusUIState.Start
    }

    fun clearGetJobStatus() {
        _getJobStatus.value = JobDetailStatusUIState.Start
    }

    fun clearGetAllJobTagsStatus() {
        _getAllJobTagsStatus.value = JobTagStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val jobRepository = application.container.jobCompanyRepository
                val loginRepository = application.container.loginRegistRepository
                CreateUpdateJobViewModel(jobRepository, loginRepository)
            }
        }
    }
}