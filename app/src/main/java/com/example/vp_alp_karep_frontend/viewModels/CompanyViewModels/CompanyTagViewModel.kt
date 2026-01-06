package com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todolistapp.models.GeneralResponseCompanyModel
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyResponse
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyTagsModel
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyTagsResponse
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyTagRepositoryInterface
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.CompanyTagStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyTagViewModel(
    private val companyTagRepository: CompanyTagRepositoryInterface,
    private val companyRepository: CompanyRepositoryInterface
): ViewModel() {

    var getAllTagsStatus: CompanyTagStatusUIState by mutableStateOf(CompanyTagStatusUIState.Start)
        private set

    var getCompanyProfileStatus: CompanyTagStatusUIState by mutableStateOf(CompanyTagStatusUIState.Start)
        private set

    var updateTagsStatus: StringDataStatusUIState by mutableStateOf(StringDataStatusUIState.Start)
        private set

    var selectedTagIds by mutableStateOf<Set<Int>>(emptySet())
        private set

    var companyId by mutableStateOf<Int>(0)
        private set

    fun getAllTags(token: String) {
        getAllTagsStatus = CompanyTagStatusUIState.Loading
        companyTagRepository.getAllCompanyTags(token).enqueue(object : Callback<CompanyTagsResponse> {
            override fun onResponse(
                call: Call<CompanyTagsResponse>,
                response: Response<CompanyTagsResponse>
            ) {
                if (response.isSuccessful) {
                    val tags = response.body()?.data ?: emptyList()
                    getAllTagsStatus = CompanyTagStatusUIState.Success(tags)
                    Log.d("CompanyTagViewModel", "Get all tags success: ${tags.size} tags")
                } else {
                    getAllTagsStatus = CompanyTagStatusUIState.Failed("Failed to get tags")
                    Log.e("CompanyTagViewModel", "Get all tags failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CompanyTagsResponse>, t: Throwable) {
                getAllTagsStatus = CompanyTagStatusUIState.Failed(t.message.toString())
                Log.e("CompanyTagViewModel", "Get all tags error: ${t.message}")
            }
        })
    }

    fun getCompanyProfile(token: String) {
        getCompanyProfileStatus = CompanyTagStatusUIState.Loading
        companyRepository.getCompanyProfile(token).enqueue(object : Callback<CompanyResponse> {
            override fun onResponse(
                call: Call<CompanyResponse>,
                response: Response<CompanyResponse>
            ) {
                if (response.isSuccessful) {
                    val company = response.body()?.data
                    if (company != null) {
                        companyId = company.id
                        selectedTagIds = company.company_tags.map { it.id }.toSet()
                        getCompanyProfileStatus = CompanyTagStatusUIState.Success(company.company_tags)
                        Log.d("CompanyTagViewModel", "Get company profile success: ${company.company_tags.size} selected tags")
                    } else {
                        getCompanyProfileStatus = CompanyTagStatusUIState.Failed("Company data is null")
                    }
                } else {
                    getCompanyProfileStatus = CompanyTagStatusUIState.Failed("Failed to get company profile")
                    Log.e("CompanyTagViewModel", "Get company profile failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CompanyResponse>, t: Throwable) {
                getCompanyProfileStatus = CompanyTagStatusUIState.Failed(t.message.toString())
                Log.e("CompanyTagViewModel", "Get company profile error: ${t.message}")
            }
        })
    }

    fun toggleTagSelection(tagId: Int) {
        selectedTagIds = if (selectedTagIds.contains(tagId)) {
            selectedTagIds - tagId
        } else {
            selectedTagIds + tagId
        }
        Log.d("CompanyTagViewModel", "Toggle tag $tagId, selected: $selectedTagIds")
    }

    fun updateTags(token: String, currentTags: List<CompanyTagsModel>) {
        updateTagsStatus = StringDataStatusUIState.Loading

        val currentTagIds = currentTags.map { it.id }.toSet()
        val toDelete = currentTagIds - selectedTagIds
        val toCreate = selectedTagIds - currentTagIds

        Log.d("CompanyTagViewModel", "Update tags - Delete: $toDelete, Create: $toCreate")

        // First, delete removed tags
        var deleteCount = 0
        var hasError = false
        val totalOperations = toDelete.size + toCreate.size

        if (totalOperations == 0) {
            updateTagsStatus = StringDataStatusUIState.Success("No changes to update")
            return
        }

        // Delete tags
        toDelete.forEach { tagId ->
            companyTagRepository.deleteCompanyToTags(token, tagId).enqueue(object : Callback<GeneralResponseCompanyModel> {
                override fun onResponse(
                    call: Call<GeneralResponseCompanyModel>,
                    response: Response<GeneralResponseCompanyModel>
                ) {
                    deleteCount++
                    if (!response.isSuccessful) {
                        hasError = true
                        Log.e("CompanyTagViewModel", "Delete tag $tagId failed: ${response.code()}")
                    }

                    // Check if all deletes are done
                    if (deleteCount == toDelete.size) {
                        if (hasError) {
                            updateTagsStatus = StringDataStatusUIState.Failed("Failed to delete some tags")
                        } else if (toCreate.isEmpty()) {
                            updateTagsStatus = StringDataStatusUIState.Success("Tags updated successfully")
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
                        updateTagsStatus = StringDataStatusUIState.Failed("Error deleting tags: ${t.message}")
                    }
                    Log.e("CompanyTagViewModel", "Delete tag error: ${t.message}")
                }
            })
        }

        // If no tags to delete, directly create new tags
        if (toDelete.isEmpty() && toCreate.isNotEmpty()) {
            createNewTags(token, toCreate, companyId)
        }
    }

    private fun createNewTags(token: String, tagIds: Set<Int>, companyId: Int) {
        var createCount = 0
        var hasError = false

        tagIds.forEach { tagId ->
            companyTagRepository.createCompanyToTags(token, companyId, tagId).enqueue(object : Callback<GeneralResponseCompanyModel> {
                override fun onResponse(
                    call: Call<GeneralResponseCompanyModel>,
                    response: Response<GeneralResponseCompanyModel>
                ) {
                    createCount++
                    if (!response.isSuccessful) {
                        hasError = true
                        Log.e("CompanyTagViewModel", "Create tag $tagId failed: ${response.code()}")
                    }

                    if (createCount == tagIds.size) {
                        if (!hasError) {
                            updateTagsStatus = StringDataStatusUIState.Success("Tags updated successfully")
                        } else {
                            updateTagsStatus = StringDataStatusUIState.Failed("Failed to create some tags")
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponseCompanyModel>, t: Throwable) {
                    hasError = true
                    updateTagsStatus = StringDataStatusUIState.Failed("Error creating tags: ${t.message}")
                    Log.e("CompanyTagViewModel", "Create tag error: ${t.message}")
                }
            })
        }
    }

    fun clearUpdateTagsStatus() {
        updateTagsStatus = StringDataStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KarepApplication)
                val companyTagRepository = application.container.companyTagRepository
                val companyRepository = application.container.companyRepository
                CompanyTagViewModel(
                    companyTagRepository = companyTagRepository,
                    companyRepository = companyRepository
                )
            }
        }
    }
}