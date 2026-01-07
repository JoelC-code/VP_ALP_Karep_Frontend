package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.repositories.ApplicationRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.ApplicationActionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicationActionViewModel(
    private val repository: ApplicationRepositoryInterface,
    private val loginRepo: LoginRegistRepository
): ViewModel() {
    private val _applyState = MutableStateFlow<ApplicationActionUiState>(ApplicationActionUiState.Idle)
    val applyState: StateFlow<ApplicationActionUiState> = _applyState

    fun applyJob(jobId: Int) {
        _applyState.value = ApplicationActionUiState.Loading

        viewModelScope.launch {
            val token = loginRepo.getAuthToken().firstOrNull() ?: run {
                _applyState.value = ApplicationActionUiState.Error("Authentication token not found")
                return@launch
            }

            val call = repository.hiringApply(token, jobId)
            call.enqueue(object : Callback<GeneralResponseModel> {
                override fun onResponse(
                    call: Call<GeneralResponseModel?>,
                    response: Response<GeneralResponseModel?>
                ) {
                    if (response.isSuccessful) {
                        _applyState.value = ApplicationActionUiState.Success
                    } else {
                        _applyState.value =
                            ApplicationActionUiState.Error("Error ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<GeneralResponseModel?>, t: Throwable) {
                    _applyState.value =
                        ApplicationActionUiState.Error(t.localizedMessage ?: "Network Error")
                }
            })
        }
    }

    fun resetStatus() {
        _applyState.value = ApplicationActionUiState.Idle
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as KarepApplication
                val repo = app.container.applicationRepository
                val logRepo = app.container.loginRegistRepository
                ApplicationActionViewModel(repo, logRepo)
            }
        }
    }
}