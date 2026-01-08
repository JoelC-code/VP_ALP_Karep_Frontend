package com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.ErrorModel
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.CompanyStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyViewModel(
    private val companyRepository: CompanyRepositoryInterface,
    private val loginRepository: LoginRegistRepository
): ViewModel() {
    private val _getCompanyProfileStatus = MutableStateFlow<CompanyStatusUIState>(
        CompanyStatusUIState.Start
    )
    val getCompanyProfileStatus = _getCompanyProfileStatus

    fun getCompanyProfile() {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _getCompanyProfileStatus.value =
                    CompanyStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _getCompanyProfileStatus.value = CompanyStatusUIState.Loading

            try {
                val call = companyRepository.getCompanyProfile(token)

                call.enqueue(object: Callback<CompanyResponse>{
                    override fun onResponse(
                        call: Call<CompanyResponse>,
                        res: Response<CompanyResponse>
                    ) {
                        if (res.isSuccessful) {
                            _getCompanyProfileStatus.value = CompanyStatusUIState.Success(
                                res.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                res.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            _getCompanyProfileStatus.value = CompanyStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }

                    override fun onFailure(call: Call<CompanyResponse?>, t: Throwable) {
                        _getCompanyProfileStatus.value = CompanyStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _getCompanyProfileStatus.value = CompanyStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun clearGetCompanyProfileErrorMessage() {
        _getCompanyProfileStatus.value = CompanyStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val companyRepository = application.container.companyRepository
                val loginRepository = application.container.loginRegistRepository
                CompanyViewModel(companyRepository, loginRepository)
            }
        }
    }
}