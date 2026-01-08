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
import com.example.vp_alp_karep_frontend.models.CompanyModels.ApplicationResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.ErrorModel
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.ApplicationCompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.ApplicationStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Callback
import okio.IOException
import retrofit2.Call
import retrofit2.Response

class ApplicationViewModel(
    private val applicationRepository: ApplicationCompanyRepositoryInterface,
    private val loginRepository: LoginRegistRepository
): ViewModel() {
    private val _getApplicationsStatus = MutableStateFlow<ApplicationStatusUIState>(
        ApplicationStatusUIState.Start
    )
    val getApplicationsStatus = _getApplicationsStatus

    private val _acceptApplicationStatus = MutableStateFlow<StringDataStatusUIState>(
        StringDataStatusUIState.Start
    )
    val acceptApplicationStatus = _acceptApplicationStatus

    private val _rejectApplicationStatus = MutableStateFlow<StringDataStatusUIState>(
        StringDataStatusUIState.Start
    )
    val rejectApplicationStatus = _rejectApplicationStatus

    fun getApplications() {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _getApplicationsStatus.value =
                    ApplicationStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _getApplicationsStatus.value = ApplicationStatusUIState.Loading

            try {
                val call = applicationRepository.getApplications(token)

                call.enqueue(object : Callback<ApplicationResponse> {
                    override fun onResponse(
                        call: Call<ApplicationResponse?>,
                        res: Response<ApplicationResponse?>
                    ) {
                        if (res.isSuccessful) {
                            _getApplicationsStatus.value = ApplicationStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            _getApplicationsStatus.value = ApplicationStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<ApplicationResponse?>,
                        t: Throwable
                    ) {
                        _getApplicationsStatus.value =
                            ApplicationStatusUIState.Failed(t.localizedMessage ?: "Unknown error")
                    }
                })
            } catch (error: IOException) {
                _getApplicationsStatus.value =
                    ApplicationStatusUIState.Failed(error.localizedMessage ?: "Network error")
            }
        }
    }

    fun acceptApplication(
        applicationId: Int
    ) {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _acceptApplicationStatus.value =
                    StringDataStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _acceptApplicationStatus.value = StringDataStatusUIState.Loading

            try {
                val call = applicationRepository.acceptApplication(
                    token,
                    applicationId
                )

                call.enqueue(object : Callback<GeneralResponseCompanyModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseCompanyModel>,
                        res: Response<GeneralResponseCompanyModel>
                    ) {
                        if (res.isSuccessful) {
                            _acceptApplicationStatus.value =
                                StringDataStatusUIState.Success(res.body()!!.data)
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            _acceptApplicationStatus.value = StringDataStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<GeneralResponseCompanyModel>,
                        t: Throwable
                    ) {
                        _acceptApplicationStatus.value =
                            StringDataStatusUIState.Failed(t.localizedMessage ?: "Unknown error")
                    }

                })

            } catch (error: IOException) {
                _acceptApplicationStatus.value =
                    StringDataStatusUIState.Failed(error.localizedMessage ?: "Network error")
            }
        }
    }

    fun rejectApplication(
        applicationId: Int
    ) {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _rejectApplicationStatus.value =
                    StringDataStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _rejectApplicationStatus.value = StringDataStatusUIState.Loading

            try {
                val call = applicationRepository.rejectApplication(
                    token,
                    applicationId
                )

                call.enqueue(object: Callback<GeneralResponseCompanyModel>{
                    override fun onResponse(
                        call: Call<GeneralResponseCompanyModel>,
                        res: Response<GeneralResponseCompanyModel>
                    ) {
                        if (res.isSuccessful) {
                            _rejectApplicationStatus.value = StringDataStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            _rejectApplicationStatus.value = StringDataStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }
                    override fun onFailure(
                        call: Call<GeneralResponseCompanyModel>,
                        t: Throwable
                    ) {
                        _rejectApplicationStatus.value = StringDataStatusUIState.Failed(t.localizedMessage ?: "Unknown error")
                    }

                })

            } catch (error: IOException) {
                _rejectApplicationStatus.value = StringDataStatusUIState.Failed(error.localizedMessage ?: "Network error")
            }
        }
    }

    fun clearGetApplicationsErrorMessage() {
        _getApplicationsStatus.value = ApplicationStatusUIState.Start
    }

    fun clearAcceptApplicationStatusErrorMessage() {
        _acceptApplicationStatus.value = StringDataStatusUIState.Start
    }

    fun clearRejectApplicationStatusErrorMessage() {
        _rejectApplicationStatus.value = StringDataStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val applicationRepository = application.container.applicationCompanyRepository
                val loginRepository = application.container.loginRegistRepository
                ApplicationViewModel(applicationRepository, loginRepository)
            }
        }
    }
}