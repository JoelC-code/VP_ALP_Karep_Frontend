package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.GetAllJobsResponse
import com.example.vp_alp_karep_frontend.repositories.JobRepositoryInterface
import com.example.vp_alp_karep_frontend.uiStates.JobListUiStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobListViewModel(
    private val repository: JobRepositoryInterface
): ViewModel() {
    private val _uiStates = MutableStateFlow<JobListUiStates>(JobListUiStates.Start)
    val uiStates: StateFlow<JobListUiStates> = _uiStates

    fun loadJobsAll(token: String) {
        if(_uiStates.value is JobListUiStates.Loading) return

        viewModelScope.launch {
            _uiStates.value = JobListUiStates.Loading

            val call = repository.getAllJobs(token)
            call.enqueue(object : Callback<GetAllJobsResponse> {
                override fun onResponse(
                    call: Call<GetAllJobsResponse>,
                    response: Response<GetAllJobsResponse>
                ) {
                    if(response.isSuccessful) {
                        _uiStates.value = JobListUiStates.Success(response.body()?.data ?: emptyList())
                    } else {
                        _uiStates.value = JobListUiStates.Error("Error ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<GetAllJobsResponse>,
                    t: Throwable
                ) {
                    _uiStates.value = JobListUiStates.Error(t.localizedMessage ?: "Network Error")
                }
            })
        }
    }

    fun loadJobsOfCompany(token: String) {
        if(_uiStates.value is JobListUiStates.Loading) return

        viewModelScope.launch {
            _uiStates.value = JobListUiStates.Loading

            val call = repository.jobByCompany(token)
            call.enqueue(object : Callback<GetAllJobsResponse>{
                override fun onResponse(
                    call: Call<GetAllJobsResponse?>,
                    response: Response<GetAllJobsResponse?>
                ) {
                    if(response.isSuccessful) {
                        _uiStates.value = JobListUiStates.Success(response.body()?.data ?: emptyList())
                    } else {
                        _uiStates.value = JobListUiStates.Error("Error ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<GetAllJobsResponse>,
                    t: Throwable) {
                    _uiStates.value = JobListUiStates.Error(t.localizedMessage ?: "Network Error")
                }
            })
        }
    }

    fun loadJobsOnSearch(token: String, search: String? = null) {
        _uiStates.value = JobListUiStates.Loading
        val call = if(search.isNullOrBlank()) {
            repository.getAllJobs(token)
        } else {
            repository.searchJobs(token, search)
        }

        call.enqueue(object : Callback<GetAllJobsResponse> {
            override fun onResponse(
                call: Call<GetAllJobsResponse?>,
                response: Response<GetAllJobsResponse?>
            ) {
                if(response.isSuccessful) {
                    _uiStates.value = JobListUiStates.Success(response.body()?.data ?: emptyList())
                } else {
                    _uiStates.value = JobListUiStates.Error("Error ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GetAllJobsResponse?>, t: Throwable) {
                _uiStates.value = JobListUiStates.Error(t.localizedMessage ?: "Network Error")
            }
        })
    }

    fun resetState() {
        _uiStates.value = JobListUiStates.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as KarepApplication
                val repository = app.container.jobRepository
                JobListViewModel(repository)
            }
        }
    }
}