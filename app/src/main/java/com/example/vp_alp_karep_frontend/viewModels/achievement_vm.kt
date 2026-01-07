package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_karep_frontend.models.CreateAchievementRequest
import com.example.vp_alp_karep_frontend.models.UpdateAchievementRequest
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.repositories.UserAchievementRepository
import com.example.vp_alp_karep_frontend.uiStates.AchievementUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AchievementViewModel(
    private val achievementRepository: UserAchievementRepository,
    private val loginRepository: LoginRegistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AchievementUiState())
    val uiState: StateFlow<AchievementUiState> = _uiState.asStateFlow()

    init {
        loadAchievements()
    }

    fun loadAchievements() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                if (token != null) {
                    val response = achievementRepository.getAllAchievements(token)

                    if (response.isSuccessful) {
                        val achievements = response.body()?.data ?: emptyList()
                        _uiState.value = _uiState.value.copy(
                            achievements = achievements,
                            isLoading = false,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal memuat achievement: ${response.message()}"
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

    fun createAchievement(title: String, description: String?) {
        if (title.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Title tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                if (token != null) {
                    val response = achievementRepository.createAchievement(
                        token = token,
                        createAchievementRequest = CreateAchievementRequest(
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
                        loadAchievements() // Reload list
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal menambah achievement: ${response.message()}"
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

    fun updateAchievement(id: Int, title: String, description: String?) {
        if (title.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Title tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                if (token != null) {
                    val response = achievementRepository.updateAchievement(
                        token = token,
                        id = id,
                        updateAchievementRequest = UpdateAchievementRequest(
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
                        loadAchievements() // Reload list
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal update achievement: ${response.message()}"
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

    fun deleteAchievement(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                if (token != null) {
                    val response = achievementRepository.deleteAchievement(token, id)

                    if (response.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isOperationSuccessful = true,
                            errorMessage = null
                        )
                        loadAchievements() // Reload list
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal hapus achievement: ${response.message()}"
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

