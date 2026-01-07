package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.GetJobResponse
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_karep_frontend.repositories.JobRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.JobDetailUiStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobDetailViewModel(
    private val loginRepository: LoginRegistRepository,
    private val repository: JobRepositoryInterface
): ViewModel() {
    private val _uiStates = MutableStateFlow<JobDetailUiStates>(JobDetailUiStates.start)
    val uiStates: StateFlow<JobDetailUiStates> = _uiStates

    fun loadJobDetail(jobId: Int) {
        if(_uiStates.value is JobDetailUiStates.Loading) return

        viewModelScope.launch {
            _uiStates.value = JobDetailUiStates.Loading

            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _uiStates.value = JobDetailUiStates.Error("Authentication token not found")
                return@launch
            }

            val call = repository.getJob(token, jobId)

            call.enqueue(object : Callback<GetJobResponse> {
                override fun onResponse(
                    call: Call<GetJobResponse?>,
                    response: Response<GetJobResponse?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        _uiStates.value = JobDetailUiStates.Success(response.body()!!.data)
                    } else {
                        _uiStates.value = JobDetailUiStates.Error("Error ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<GetJobResponse?>, t: Throwable) {
                    _uiStates.value = JobDetailUiStates.Error(t.localizedMessage ?: "Network Error")
                }
            })
        }
    }

    fun resetState() {
        _uiStates.value = JobDetailUiStates.start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as KarepApplication
                val repository = app.container.jobRepository
                val loginRepo = app.container.loginRegistRepository
                JobDetailViewModel(loginRepo, repository)
            }
        }
    }
}