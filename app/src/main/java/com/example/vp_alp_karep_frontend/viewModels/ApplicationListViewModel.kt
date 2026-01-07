package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.GetAllApplicationResponse
import com.example.vp_alp_karep_frontend.models.GetApplicationResponse
import com.example.vp_alp_karep_frontend.repositories.ApplicationRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.ApplicationListUiStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicationListViewModel(
    private val repository: ApplicationRepositoryInterface,
    private val loginRepo: LoginRegistRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<ApplicationListUiStates>(ApplicationListUiStates.Start)
    val uiState: StateFlow<ApplicationListUiStates> = _uiState

    fun loadApplications() {
        _uiState.value = ApplicationListUiStates.Loading
        viewModelScope.launch {
            val token = loginRepo.getAuthToken().firstOrNull() ?: run {
                _uiState.value = ApplicationListUiStates.Error("Authentication token not found")
                return@launch
            }
            val call = repository.getMyApplications(token)
            call.enqueue(object : Callback<GetAllApplicationResponse> {
                override fun onResponse(
                    call: Call<GetAllApplicationResponse?>,
                    response: Response<GetAllApplicationResponse?>
                ) {
                    if (response.isSuccessful) {
                        _uiState.value =
                            ApplicationListUiStates.Success(response.body()?.data ?: emptyList())
                    } else {
                        _uiState.value = ApplicationListUiStates.Error("Error ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<GetAllApplicationResponse?>, t: Throwable) {
                    _uiState.value =
                        ApplicationListUiStates.Error(t.localizedMessage ?: "Network Error")
                }
            })
        }
    }

    fun cancelApplication(id: Int) {
        _uiState.value = ApplicationListUiStates.Loading

        viewModelScope.launch {
            val token = loginRepo.getAuthToken().firstOrNull() ?: run {
                _uiState.value = ApplicationListUiStates.Error("Authentication token not found")
                return@launch
            }
            val call = repository.cancelApplication(token, id)
            call.enqueue(object : Callback<GetApplicationResponse> {
                override fun onResponse(
                    call: Call<GetApplicationResponse>,
                    response: Response<GetApplicationResponse>
                ) {
                    if (response.isSuccessful) {
                        loadApplications()
                    } else {
                        _uiState.value = ApplicationListUiStates.Error("Error ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<GetApplicationResponse>, t: Throwable) {
                    _uiState.value =
                        ApplicationListUiStates.Error(t.localizedMessage ?: "Network Error")
                }
            })
        }
    }

    fun deleteApplication(id: Int) {
        _uiState.value = ApplicationListUiStates.Loading

        viewModelScope.launch {
            val token = loginRepo.getAuthToken().firstOrNull() ?: run {
                _uiState.value = ApplicationListUiStates.Error("Authentication token not found")
                return@launch
            }
            repository.deleteApplication(token, id)
                .enqueue(object : Callback<GeneralResponseModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseModel?>,
                        response: Response<GeneralResponseModel?>
                    ) {
                        if(response.isSuccessful) {
                            loadApplications()
                        } else {
                            _uiState.value = ApplicationListUiStates.Error("Action failed (${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponseModel?>, t: Throwable) {
                        _uiState.value = ApplicationListUiStates.Error(t.localizedMessage!!)
                    }
                })
        }
    }


    fun resetState() {
        _uiState.value = ApplicationListUiStates.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as KarepApplication
                val repo = app.container.applicationRepository
                val loginRepo = app.container.loginRegistRepository
                ApplicationListViewModel(repo, loginRepo)
            }
        }
    }
}