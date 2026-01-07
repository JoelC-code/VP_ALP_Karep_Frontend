package com.example.vp_alp_karep_frontend.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_karep_frontend.models.UpdateEmailRequest
import com.example.vp_alp_karep_frontend.models.UpdatePasswordRequest
import com.example.vp_alp_karep_frontend.models.UpdateProfileRequest
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.repositories.UserProfileRepository
import com.example.vp_alp_karep_frontend.uiStates.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val profileRepository: UserProfileRepository,
    private val loginRepository: LoginRegistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                Log.d("UserProfileVM", "Loading profile, token: ${token?.take(20)}...")

                if (token != null) {
                    val response = profileRepository.getProfile(token)
                    Log.d("UserProfileVM", "Profile response code: ${response.code()}")

                    if (response.isSuccessful) {
                        val user = response.body()?.data
                        Log.d("UserProfileVM", "Profile loaded: username=${user?.username}, email=${user?.email}")
                        _uiState.value = _uiState.value.copy(
                            user = user,
                            isLoading = false,
                            errorMessage = null
                        )
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("UserProfileVM", "Failed to load profile: ${response.code()} - $errorBody")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal memuat profile: ${response.message()}"
                        )
                    }
                } else {
                    Log.e("UserProfileVM", "Token not found")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Token tidak ditemukan. Silakan login kembali."
                    )
                }
            } catch (e: Exception) {
                Log.e("UserProfileVM", "Exception loading profile: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun updateProfile(
        fullName: String?,
        phoneNumber: String?,
        address: String?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                Log.d("UserProfileVM", "Updating profile - fullName: $fullName, phone: $phoneNumber, address: $address")

                if (token != null) {
                    val updateRequest = UpdateProfileRequest(
                        fullName = fullName,
                        phoneNumber = phoneNumber,
                        address = address
                    )

                    Log.d("UserProfileVM", "Sending update request with token: ${token.take(20)}...")
                    val response = profileRepository.updateProfile(
                        token = token,
                        updateProfileRequest = updateRequest
                    )

                    Log.d("UserProfileVM", "Update response code: ${response.code()}")

                    if (response.isSuccessful) {
                        val updatedUser = response.body()?.data
                        Log.d("UserProfileVM", "Update successful! Updated user: fullName=${updatedUser?.fullName}")

                        _uiState.value = _uiState.value.copy(
                            user = updatedUser,
                            isLoading = false,
                            isUpdateSuccessful = true,
                            successMessage = "Profile berhasil diupdate",
                            errorMessage = null
                        )
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("UserProfileVM", "Failed to update profile: ${response.code()} - $errorBody")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal update profile: ${errorBody ?: response.message()}"
                        )
                    }
                } else {
                    Log.e("UserProfileVM", "Token not found for update")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Token tidak ditemukan. Silakan login kembali."
                    )
                }
            } catch (e: Exception) {
                Log.e("UserProfileVM", "Exception updating profile: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun updateEmail(currentPassword: String, newEmail: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                Log.d("UserProfileVM", "Updating email to: $newEmail")

                if (token != null) {
                    val updateRequest = UpdateEmailRequest(
                        currentPassword = currentPassword,
                        newEmail = newEmail
                    )

                    val response = profileRepository.updateEmail(token, updateRequest)
                    Log.d("UserProfileVM", "Update email response code: ${response.code()}")

                    if (response.isSuccessful) {
                        val emailResponse = response.body()?.data
                        Log.d("UserProfileVM", "Email updated successfully: ${emailResponse?.email}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isUpdateSuccessful = true,
                            successMessage = emailResponse?.message ?: "Email berhasil diupdate",
                            errorMessage = null
                        )
                        loadProfile()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("UserProfileVM", "Failed to update email: ${response.code()} - $errorBody")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal update email: ${errorBody ?: response.message()}"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Token tidak ditemukan. Silakan login kembali."
                    )
                }
            } catch (e: Exception) {
                Log.e("UserProfileVM", "Exception updating email: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun updatePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)

            try {
                val token = loginRepository.getAuthToken().firstOrNull()
                Log.d("UserProfileVM", "Updating password")

                if (token != null) {
                    val updateRequest = UpdatePasswordRequest(
                        currentPassword = currentPassword,
                        newPassword = newPassword
                    )

                    val response = profileRepository.updatePassword(token, updateRequest)
                    Log.d("UserProfileVM", "Update password response code: ${response.code()}")

                    if (response.isSuccessful) {
                        val passwordResponse = response.body()?.data
                        Log.d("UserProfileVM", "Password updated successfully")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isUpdateSuccessful = true,
                            successMessage = passwordResponse?.message ?: "Password berhasil diupdate",
                            errorMessage = null
                        )
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("UserProfileVM", "Failed to update password: ${response.code()} - $errorBody")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Gagal update password: ${errorBody ?: response.message()}"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Token tidak ditemukan. Silakan login kembali."
                    )
                }
            } catch (e: Exception) {
                Log.e("UserProfileVM", "Exception updating password: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginRepository.clearAuthData()
            _uiState.value = ProfileUiState()
        }
    }

    fun resetUpdateStatus() {
        _uiState.value = _uiState.value.copy(
            isUpdateSuccessful = false,
            successMessage = null,
            errorMessage = null
        )
    }
}