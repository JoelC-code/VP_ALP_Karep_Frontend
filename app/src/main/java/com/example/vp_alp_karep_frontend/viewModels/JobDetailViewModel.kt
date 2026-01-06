package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_karep_frontend.KarepApplication
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.vp_alp_karep_frontend.models.JobResponse
import com.example.vp_alp_karep_frontend.repositories.JobRepositoryInterface
import com.example.vp_alp_karep_frontend.uiStates.JobDetailUiStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobDetailViewModel(
    private val repository: JobRepositoryInterface
): ViewModel() {
    private val _uiStates = MutableStateFlow<JobDetailUiStates>(JobDetailUiStates.start)
    val uiStates: StateFlow<JobDetailUiStates> = _uiStates

    fun loadJobDetail(token: String, jobId: Int) {
        if(_uiStates.value is JobDetailUiStates.Loading) return

        _uiStates.value = JobDetailUiStates.Loading

        val call = repository.getJob(token, jobId)

        call.enqueue(object : Callback<JobResponse> {
            override fun onResponse(call: Call<JobResponse?>, response: Response<JobResponse?>) {
                if(response.isSuccessful && response.body() != null) {
                    _uiStates.value = JobDetailUiStates.Success(response.body()!!.data)
                } else {
                    _uiStates.value = JobDetailUiStates.Error("Error ${response.code()}")
                }
            }

            override fun onFailure(call: Call<JobResponse?>, t: Throwable) {
                _uiStates.value = JobDetailUiStates.Error(t.localizedMessage ?: "Network Error")
            }
        })
    }

    fun resetState() {
        _uiStates.value = JobDetailUiStates.start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as KarepApplication
                val repository = app.container.jobRepository
                JobDetailViewModel(repository)
            }
        }
    }
}