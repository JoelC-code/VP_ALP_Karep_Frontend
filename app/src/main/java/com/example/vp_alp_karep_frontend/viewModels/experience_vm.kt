package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_karep_frontend.models.CreateExperienceRequest
import com.example.vp_alp_karep_frontend.models.UpdateExperienceRequest
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.repositories.UserExperienceRepository
import com.example.vp_alp_karep_frontend.uiStates.ExperienceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ExperienceViewModel(
    private val experienceRepository: UserExperienceRepository,
    private val loginRepository: LoginRegistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExperienceUiState())
    val uiState: StateFlow<ExperienceUiState> = _uiState.asStateFlow()

    init {
        loadExperiences()
    }

    fun loadExperiences() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                if (token != null) {
                    val response = experienceRepository.getAllExperiences(token)

                    if (response.isSuccessful) {
                        val experiences = response.body()?.data ?: emptyList()
                        _uiState.value = _uiState.value.copy(
                            experiences = experiences,
                            isLoading = false,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal memuat experience: ${response.message()}"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Token tidak ditemukan"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun createExperience(title: String, description: String?) {
        if (title.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Title tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                if (token != null) {
                    val response = experienceRepository.createExperience(
                        token = token,
                        createExperienceRequest = CreateExperienceRequest(
                            title = title,
                            description = description
                        )
                    )

                    if (response.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isOperationSuccessful = true,
                            errorMessage = null
                        )
                        loadExperiences() // Reload list
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal menambah experience: ${response.message()}"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Token tidak ditemukan"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun updateExperience(id: Int, title: String, description: String?) {
        if (title.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Title tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                if (token != null) {
                    val response = experienceRepository.updateExperience(
                        token = token,
                        id = id,
                        updateExperienceRequest = UpdateExperienceRequest(
                            title = title,
                            description = description
                        )
                    )

                    if (response.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isOperationSuccessful = true,
                            errorMessage = null
                        )
                        loadExperiences() // Reload list
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal update experience: ${response.message()}"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Token tidak ditemukan"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun deleteExperience(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                if (token != null) {
                    val response = experienceRepository.deleteExperience(token, id)

                    if (response.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isOperationSuccessful = true,
                            errorMessage = null
                        )
                        loadExperiences() // Reload list
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal hapus experience: ${response.message()}"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Token tidak ditemukan"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun resetOperationStatus() {
        _uiState.value = _uiState.value.copy(isOperationSuccessful = false, errorMessage = null)
    }
}

