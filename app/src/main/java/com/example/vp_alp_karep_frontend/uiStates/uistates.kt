package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.*

// Generic UI State
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// Login UI State
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

// Register UI State
data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisterSuccessful: Boolean = false
)

// Profile UI State
data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUpdateSuccessful: Boolean = false,
    val successMessage: String? = null
)

// Experience UI State
data class ExperienceUiState(
    val experiences: List<Experience> = emptyList(),
    val selectedExperience: Experience? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOperationSuccessful: Boolean = false
)

// Achievement UI State
data class AchievementUiState(
    val achievements: List<Achievement> = emptyList(),
    val selectedAchievement: Achievement? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOperationSuccessful: Boolean = false
)
