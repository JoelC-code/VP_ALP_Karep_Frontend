package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_karep_frontend.models.GetAllJobTagResponse
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.repositories.JobTagRepositoryInterface
import com.example.vp_alp_karep_frontend.uiStates.JobTagUiStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobTagListViewModel(
    private val repository: JobTagRepositoryInterface
): ViewModel() {
    private val _uiStates = MutableStateFlow<JobTagUiStates>(JobTagUiStates.Start)
    val uiStates: StateFlow<JobTagUiStates> = _uiStates

    fun loadJobTags(token: String) {
        if(_uiStates.value is JobTagUiStates.Loading) return

        viewModelScope.launch {
            _uiStates.value = JobTagUiStates.Loading

            val call = repository.getAllTags(token)
            call.enqueue(object : Callback<GetAllJobTagResponse> {
                override fun onResponse(
                    call: Call<GetAllJobTagResponse>,
                    response: Response<GetAllJobTagResponse>
                ) {
                    if(response.isSuccessful) {
                        _uiStates.value = JobTagUiStates.Success(response.body()?.data ?: emptyList())
                    } else {
                        _uiStates.value = JobTagUiStates.Error("Error ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<GetAllJobTagResponse>,
                    t: Throwable
                ) {
                    _uiStates.value = JobTagUiStates.Error(t.localizedMessage ?: "Network Error")
                }
            })
        }
    }

    fun resetState() {
        _uiStates.value = JobTagUiStates.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as KarepApplication
                val repository = app.container.applicationRepository
                ApplicationListViewModel(repository)
            }
        }
    }
}