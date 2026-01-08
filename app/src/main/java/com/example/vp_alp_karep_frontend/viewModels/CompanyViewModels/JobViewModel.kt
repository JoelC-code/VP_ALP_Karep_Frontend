package com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.CompanyModels.ErrorModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobListResponse
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.JobCompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.JobStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobViewModel(
    private val jobRepository: JobCompanyRepositoryInterface,
    private val loginRepository: LoginRegistRepository
): ViewModel() {
    private val _getAllJobsStatus = MutableStateFlow<JobStatusUIState>(
        JobStatusUIState.Start
    )
    val getAllJobsStatus = _getAllJobsStatus

    private val _deleteJobStatus = MutableStateFlow<StringDataStatusUIState>(
        StringDataStatusUIState.Start
    )
    val deleteJobStatus = _deleteJobStatus

    fun getAllJobsByCompany() {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _getAllJobsStatus.value =
                    JobStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _getAllJobsStatus.value = JobStatusUIState.Loading

            try {
                val call = jobRepository.getAllJobsByCompany(token)

                call.enqueue(object: Callback<JobListResponse> {
                    override fun onResponse(
                        call: Call<JobListResponse>,
                        res: Response<JobListResponse>
                    ) {
                        if (res.isSuccessful) {
                            _getAllJobsStatus.value = JobStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            _getAllJobsStatus.value = JobStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<JobListResponse>,
                        t: Throwable
                    ) {
                        _getAllJobsStatus.value = JobStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _getAllJobsStatus.value = JobStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun deleteJob(jobId: Int) {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _deleteJobStatus.value =
                    StringDataStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _deleteJobStatus.value = StringDataStatusUIState.Loading

            try {
                val call = jobRepository.deleteJob(token, jobId)

                call.enqueue(object: Callback<GeneralResponseCompanyModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseCompanyModel>,
                        res: Response<GeneralResponseCompanyModel>
                    ) {
                        if (res.isSuccessful) {
                            _deleteJobStatus.value = StringDataStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            _deleteJobStatus.value = StringDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<GeneralResponseCompanyModel>,
                        t: Throwable
                    ) {
                        _deleteJobStatus.value = StringDataStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _deleteJobStatus.value = StringDataStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun clearDeleteJobErrorMessage() {
        _deleteJobStatus.value = StringDataStatusUIState.Start
    }

    fun clearGetAllJobsErrorMessage() {
        _getAllJobsStatus.value = JobStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val jobRepository = application.container.jobCompanyRepository
                val loginRepository = application.container.loginRegistRepository
                JobViewModel(jobRepository, loginRepository)
            }
        }
    }
}