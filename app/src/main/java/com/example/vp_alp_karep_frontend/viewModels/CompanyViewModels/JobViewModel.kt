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
import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.CompanyModels.ErrorModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.JobListResponse
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.JobRepositoryInterface
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.JobStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobViewModel(
    private val jobRepository: JobRepositoryInterface
): ViewModel() {
    var getAllJobsStatus: JobStatusUIState by mutableStateOf(
        JobStatusUIState.Start
    )
        private set

    var deleteJobStatus: StringDataStatusUIState by mutableStateOf(
        StringDataStatusUIState.Start
    )
        private set

    fun getAllJobsByCompany(token: String) {
        viewModelScope.launch {
            getAllJobsStatus = JobStatusUIState.Loading

            try {
                val call = jobRepository.getAllJobsByCompany(token)

                call.enqueue(object: Callback<JobListResponse> {
                    override fun onResponse(
                        call: Call<JobListResponse>,
                        res: Response<JobListResponse>
                    ) {
                        if (res.isSuccessful) {
                            getAllJobsStatus = JobStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            getAllJobsStatus = JobStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<JobListResponse>,
                        t: Throwable
                    ) {
                        getAllJobsStatus = JobStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                getAllJobsStatus = JobStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun deleteJob(token: String, jobId: Int) {
        viewModelScope.launch {
            deleteJobStatus = StringDataStatusUIState.Loading

            try {
                val call = jobRepository.deleteJob(token, jobId)

                call.enqueue(object: Callback<GeneralResponseCompanyModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseCompanyModel>,
                        res: Response<GeneralResponseCompanyModel>
                    ) {
                        if (res.isSuccessful) {
                            deleteJobStatus = StringDataStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            deleteJobStatus = StringDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<GeneralResponseCompanyModel>,
                        t: Throwable
                    ) {
                        deleteJobStatus = StringDataStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                deleteJobStatus = StringDataStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun clearDeleteJobErrorMessage() {
        deleteJobStatus = StringDataStatusUIState.Start
    }

    fun clearGetAllJobsErrorMessage() {
        getAllJobsStatus = JobStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val jobRepository = application.container.jobRepository
                JobViewModel(
                    jobRepository = jobRepository
                )
            }
        }
    }
}