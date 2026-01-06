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
import com.example.todolistapp.models.GeneralResponseModel
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.CompanyResponse
import com.example.vp_alp_karep_frontend.models.ErrorModel
import com.example.vp_alp_karep_frontend.repositories.CompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.uiStates.CompanyStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class UpdateCompanyViewModel(
    private val companyRepository: CompanyRepositoryInterface
): ViewModel() {
    var updateCompanyStatus: StringDataStatusUIState by mutableStateOf(
        StringDataStatusUIState.Start)
        private set

    var getCompanyProfileStatus: CompanyStatusUIState by mutableStateOf(
        CompanyStatusUIState.Start)
        private set

    fun getCompanyProfile(token: String) {
        viewModelScope.launch {
            getCompanyProfileStatus = CompanyStatusUIState.Loading

            try {
                val call = companyRepository.getCompanyProfile(token)

                call.enqueue(object: Callback<CompanyResponse> {
                    override fun onResponse(
                        call: Call<CompanyResponse>,
                        res: Response<CompanyResponse>
                    ) {
                        if (res.isSuccessful) {
                            getCompanyProfileStatus = CompanyStatusUIState.Success(res.body()!!.data)
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            getCompanyProfileStatus = CompanyStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<CompanyResponse>,
                        t: Throwable
                    ) {
                        getCompanyProfileStatus = CompanyStatusUIState.Failed(t.localizedMessage ?: "Unknown error")
                    }
                })
            } catch (error: IOException) {
                getCompanyProfileStatus = CompanyStatusUIState.Failed(error.localizedMessage ?: "Network error")
            }
        }
    }

    fun updateCompanyProfile(
        token: String,
        name: String,
        address: String?,
        phoneNumber: String?,
        website: String?,
        visionMission: String?,
        description: String?,
        foundingDate: Date?,
        logoPath: String?,
        imagePath: String?
    ) {
        viewModelScope.launch {
            updateCompanyStatus = StringDataStatusUIState.Loading

            try {
                val call = companyRepository.updateCompanyProfile(
                    token,
                    name,
                    address,
                    phoneNumber,
                    website,
                    visionMission,
                    description,
                    foundingDate,
                    logoPath,
                    imagePath
                )

                call.enqueue(object: Callback<GeneralResponseModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        res: Response<GeneralResponseModel>
                    ) {
                        if (res.isSuccessful) {
                            updateCompanyStatus = StringDataStatusUIState.Success(res.body()!!.data)
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            updateCompanyStatus = StringDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(
                        call: Call<GeneralResponseModel>,
                        t: Throwable
                    ) {
                        updateCompanyStatus = StringDataStatusUIState.Failed(t.localizedMessage ?: "Unknown error")
                    }
                })
            } catch (error: IOException) {
                updateCompanyStatus = StringDataStatusUIState.Failed(error.localizedMessage ?: "Network error")
            }
        }
    }

    fun clearUpdateCompanyStatusErrorMessage() {
        updateCompanyStatus = StringDataStatusUIState.Start
    }

    fun clearGetCompanyProfileErrorMessage() {
        getCompanyProfileStatus = CompanyStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val companyRepository = application.container.companyRepository
                UpdateCompanyViewModel(companyRepository)
            }
        }
    }
}

