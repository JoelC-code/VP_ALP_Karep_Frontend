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
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.ApplicationStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Callback
import okio.IOException
import retrofit2.Call
import retrofit2.Response

class ApplicationViewModel(
    private val applicationRepository: ApplicationCompanyRepositoryInterface
): ViewModel() {
    var getApplicationsStatus: ApplicationStatusUIState by mutableStateOf(
        ApplicationStatusUIState.Start)
        private set

    var acceptApplicationStatus: StringDataStatusUIState by mutableStateOf(
        StringDataStatusUIState.Start)
        private set

    var rejectApplicationStatus: StringDataStatusUIState by mutableStateOf(
        StringDataStatusUIState.Start)
        private set

    fun getApplications(
        token: String
    ) {
        viewModelScope.launch {
            getApplicationsStatus = ApplicationStatusUIState.Loading

            try {
                val call = applicationRepository.getApplications(token)

                call.enqueue(object: Callback<ApplicationResponse>{
                    override fun onResponse(
                        call: Call<ApplicationResponse?>,
                        res: Response<ApplicationResponse?>
                    ) {
                        if (res.isSuccessful) {
                            getApplicationsStatus = ApplicationStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            getApplicationsStatus = ApplicationStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<ApplicationResponse?>,
                        t: Throwable
                    ) {
                        getApplicationsStatus = ApplicationStatusUIState.Failed(t.localizedMessage ?: "Unknown error")
                    }
                })
            } catch (error: IOException) {
                getApplicationsStatus = ApplicationStatusUIState.Failed(error.localizedMessage ?: "Network error")
            }
        }
    }

    fun acceptApplication(
        token: String,
        applicationId: Int
    ) {
        viewModelScope.launch {
            acceptApplicationStatus = StringDataStatusUIState.Loading

            try {
                val call = applicationRepository.acceptApplication(
                    token,
                    applicationId
                )

                call.enqueue(object: Callback<GeneralResponseCompanyModel>{
                    override fun onResponse(
                        call: Call<GeneralResponseCompanyModel>,
                        res: Response<GeneralResponseCompanyModel>
                    ) {
                        if (res.isSuccessful) {
                            acceptApplicationStatus = StringDataStatusUIState.Success(res.body()!!.data)
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            acceptApplicationStatus = StringDataStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }
                    override fun onFailure(
                        call: Call<GeneralResponseCompanyModel>,
                        t: Throwable
                    ) {
                        acceptApplicationStatus = StringDataStatusUIState.Failed(t.localizedMessage ?: "Unknown error")
                    }

                })

            } catch (error: IOException) {
                acceptApplicationStatus = StringDataStatusUIState.Failed(error.localizedMessage ?: "Network error")
            }
        }
    }

    fun rejectApplication(
        token: String,
        applicationId: Int
    ) {
        viewModelScope.launch {
            rejectApplicationStatus = StringDataStatusUIState.Loading

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
                            rejectApplicationStatus = StringDataStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            rejectApplicationStatus = StringDataStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }
                    override fun onFailure(
                        call: Call<GeneralResponseCompanyModel>,
                        t: Throwable
                    ) {
                        rejectApplicationStatus = StringDataStatusUIState.Failed(t.localizedMessage ?: "Unknown error")
                    }

                })

            } catch (error: IOException) {
                rejectApplicationStatus = StringDataStatusUIState.Failed(error.localizedMessage ?: "Network error")
            }
        }
    }

    fun clearGetApplicationsErrorMessage() {
        getApplicationsStatus = ApplicationStatusUIState.Start
    }

    fun clearAcceptApplicationStatusErrorMessage() {
        acceptApplicationStatus = StringDataStatusUIState.Start
    }

    fun clearRejectApplicationStatusErrorMessage() {
        rejectApplicationStatus = StringDataStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val applicationRRepository = application.container.applicationCompanyRepository
                ApplicationViewModel(applicationRRepository)
            }
        }
    }
}