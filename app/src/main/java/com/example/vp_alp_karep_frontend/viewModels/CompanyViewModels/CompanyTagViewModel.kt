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
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyTagsModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyTagsResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.ErrorModel
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyTagRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.CompanyTagStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyTagViewModel(
    private val companyTagRepository: CompanyTagRepositoryInterface,
    private val companyRepository: CompanyRepositoryInterface,
    private val loginRepository: LoginRegistRepository
): ViewModel() {

    private val _getAllTagsStatus = MutableStateFlow<CompanyTagStatusUIState>(
        CompanyTagStatusUIState.Start
    )
    val getAllTagsStatus = _getAllTagsStatus

    private val _getCompanyProfileStatus = MutableStateFlow<CompanyTagStatusUIState>(
        CompanyTagStatusUIState.Start
    )
    val getCompanyProfileStatus = _getCompanyProfileStatus

    private val _updateTagsStatus = MutableStateFlow<StringDataStatusUIState>(
        StringDataStatusUIState.Start
    )
    val updateTagsStatus = _updateTagsStatus

    var selectedTagIds by mutableStateOf<Set<Int>>(emptySet())
        private set

    var companyId by mutableStateOf<Int>(0)
        private set

    fun getAllTags() {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _getAllTagsStatus.value = CompanyTagStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _getAllTagsStatus.value = CompanyTagStatusUIState.Loading

            try {
                val call = companyTagRepository.getAllCompanyTags(token)

                call.enqueue(object : Callback<CompanyTagsResponse> {
                    override fun onResponse(
                        call: Call<CompanyTagsResponse>,
                        response: Response<CompanyTagsResponse>
                    ) {
                        if (response.isSuccessful) {
                            _getAllTagsStatus.value = CompanyTagStatusUIState.Success(
                                response.body()!!.data
                            )
                        } else {
                            val errorMessage = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            _getAllTagsStatus.value = CompanyTagStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }

                    override fun onFailure(call: Call<CompanyTagsResponse>, t: Throwable) {
                        _getAllTagsStatus.value = CompanyTagStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _getAllTagsStatus.value = CompanyTagStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun getCompanyProfile() {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _getCompanyProfileStatus.value = CompanyTagStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _getCompanyProfileStatus.value = CompanyTagStatusUIState.Loading

            try {
                val call = companyRepository.getCompanyProfile(token)

                call.enqueue(object : Callback<CompanyResponse> {
                    override fun onResponse(
                        call: Call<CompanyResponse>,
                        response: Response<CompanyResponse>
                    ) {
                        if (response.isSuccessful) {
                            val company = response.body()?.data
                            if (company != null) {
                                companyId = company.id
                                selectedTagIds = company.company_tags.map { it.id }.toSet()
                                _getCompanyProfileStatus.value = CompanyTagStatusUIState.Success(
                                    company.company_tags
                                )
                            } else {
                                _getCompanyProfileStatus.value = CompanyTagStatusUIState.Failed(
                                    "Company data is null"
                                )
                            }
                        } else {
                            val errorMessage = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )

                            _getCompanyProfileStatus.value = CompanyTagStatusUIState.Failed(
                                errorMessage.errors
                            )
                        }
                    }

                    override fun onFailure(call: Call<CompanyResponse>, t: Throwable) {
                        _getCompanyProfileStatus.value = CompanyTagStatusUIState.Failed(
                            t.localizedMessage ?: "Unknown error"
                        )
                    }
                })
            } catch (error: IOException) {
                _getCompanyProfileStatus.value = CompanyTagStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    fun toggleTagSelection(tagId: Int) {
        selectedTagIds = if (selectedTagIds.contains(tagId)) {
            selectedTagIds - tagId
        } else {
            selectedTagIds + tagId
        }
    }

    fun updateTags(currentTags: List<CompanyTagsModel>) {
        viewModelScope.launch {
            val token = loginRepository.getAuthToken().firstOrNull() ?: run {
                _updateTagsStatus.value = StringDataStatusUIState.Failed("Authentication token not found")
                return@launch
            }

            _updateTagsStatus.value = StringDataStatusUIState.Loading

            try {
                val currentTagIds = currentTags.map { it.id }.toSet()
                val toDelete = currentTagIds - selectedTagIds
                val toCreate = selectedTagIds - currentTagIds

                var deleteCount = 0
                var hasError = false
                val totalOperations = toDelete.size + toCreate.size

                if (totalOperations == 0) {
                    _updateTagsStatus.value = StringDataStatusUIState.Success("No changes to update")
                    return@launch
                }

                // Delete tags
                toDelete.forEach { tagId ->
                    val call = companyTagRepository.deleteCompanyToTags(token, tagId)

                    call.enqueue(object : Callback<GeneralResponseCompanyModel> {
                        override fun onResponse(
                            call: Call<GeneralResponseCompanyModel>,
                            response: Response<GeneralResponseCompanyModel>
                        ) {
                            deleteCount++
                            if (!response.isSuccessful) {
                                hasError = true
                                val errorMessage = Gson().fromJson(
                                    response.errorBody()!!.charStream(),
                                    ErrorModel::class.java
                                )
                                _updateTagsStatus.value = StringDataStatusUIState.Failed(
                                    errorMessage.errors
                                )
                            }

                            // Check if all deletes are done
                            if (deleteCount == toDelete.size) {
                                if (hasError) {
                                    _updateTagsStatus.value = StringDataStatusUIState.Failed("Failed to delete some tags")
                                } else if (toCreate.isEmpty()) {
                                    _updateTagsStatus.value = StringDataStatusUIState.Success("Tags updated successfully")
                                } else {
                                    // Start creating new tags
                                    createNewTags(token, toCreate, companyId)
                                }
                            }
                        }

                        override fun onFailure(call: Call<GeneralResponseCompanyModel>, t: Throwable) {
                            hasError = true
                            deleteCount++
                            if (deleteCount == toDelete.size) {
                                _updateTagsStatus.value = StringDataStatusUIState.Failed(
                                    t.localizedMessage ?: "Unknown error"
                                )
                            }
                        }
                    })
                }

                // If no tags to delete, directly create new tags
                if (toDelete.isEmpty() && toCreate.isNotEmpty()) {
                    createNewTags(token, toCreate, companyId)
                }
            } catch (error: IOException) {
                _updateTagsStatus.value = StringDataStatusUIState.Failed(
                    error.localizedMessage ?: "Network error"
                )
            }
        }
    }

    private fun createNewTags(token: String, tagIds: Set<Int>, companyId: Int) {
        var createCount = 0
        var hasError = false

        tagIds.forEach { tagId ->
            val call = companyTagRepository.createCompanyToTags(token, companyId, tagId)

            call.enqueue(object : Callback<GeneralResponseCompanyModel> {
                override fun onResponse(
                    call: Call<GeneralResponseCompanyModel>,
                    response: Response<GeneralResponseCompanyModel>
                ) {
                    createCount++
                    if (!response.isSuccessful) {
                        hasError = true
                        val errorMessage = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorModel::class.java
                        )
                        _updateTagsStatus.value = StringDataStatusUIState.Failed(
                            errorMessage.errors
                        )
                    }

                    if (createCount == tagIds.size) {
                        if (!hasError) {
                            _updateTagsStatus.value = StringDataStatusUIState.Success("Tags updated successfully")
                        } else {
                            _updateTagsStatus.value = StringDataStatusUIState.Failed("Failed to create some tags")
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponseCompanyModel>, t: Throwable) {
                    hasError = true
                    _updateTagsStatus.value = StringDataStatusUIState.Failed(
                        t.localizedMessage ?: "Unknown error"
                    )
                }
            })
        }
    }

    fun clearGetAllTagsStatus() {
        _getAllTagsStatus.value = CompanyTagStatusUIState.Start
    }

    fun clearGetCompanyProfileStatus() {
        _getCompanyProfileStatus.value = CompanyTagStatusUIState.Start
    }

    fun clearUpdateTagsStatus() {
        _updateTagsStatus.value = StringDataStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val companyTagRepository = application.container.companyTagCompanyRepository
                val companyRepository = application.container.companyRepository
                val loginRepository = application.container.loginRegistRepository
                CompanyTagViewModel(
                    companyTagRepository = companyTagRepository,
                    companyRepository = companyRepository,
                    loginRepository = loginRepository
                )
            }
        }
    }
}