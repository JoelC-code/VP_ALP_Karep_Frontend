package com.example.vp_alp_karep_frontend.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.ErrorModel
import com.example.vp_alp_karep_frontend.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.models.GetAllApplicationResponse
import com.example.vp_alp_karep_frontend.models.GetApplicationResponse
import com.example.vp_alp_karep_frontend.repositories.ApplicationRepositoryInterface
import com.example.vp_alp_karep_frontend.uiStates.ApplicationStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Callback
import okio.IOException
import retrofit2.Call
import retrofit2.Response

class ApplicationCompanyViewModel(
    private val applicationRepository: ApplicationRepositoryInterface
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

                call.enqueue(object: Callback<GetAllApplicationResponse>{
                    override fun onResponse(
                        call: Call<GetAllApplicationResponse?>,
                        res: Response<GetAllApplicationResponse?>
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
                        call: Call<GetAllApplicationResponse?>,
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

                call.enqueue(object: Callback<GeneralResponseModel>{
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        res: Response<GeneralResponseModel>
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
                        call: Call<GeneralResponseModel>,
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

                call.enqueue(object: Callback<GeneralResponseModel>{
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        res: Response<GeneralResponseModel>
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
                        call: Call<GeneralResponseModel>,
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
                val applicationRRepository = application.container.applicationRepository
                ApplicationCompanyViewModel(applicationRRepository)
            }
        }
    }
}