package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_karep_frontend.models.RegisterRequest
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val loginRepository: LoginRegistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username, errorMessage = null)
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword, errorMessage = null)
    }

    fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(address = fullName, errorMessage = null)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phoneNumber, errorMessage = null)
    }

    fun register() {
        // Validation
        when {
            _uiState.value.username.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Username tidak boleh kosong")
                return
            }
            _uiState.value.email.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Email tidak boleh kosong")
                return
            }
            _uiState.value.password.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Password tidak boleh kosong")
                return
            }
            _uiState.value.password != _uiState.value.confirmPassword -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Password tidak cocok")
                return
            }
            _uiState.value.password.length < 6 -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Password minimal 6 karakter")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val addressValue = _uiState.value.address
                val phoneValue = _uiState.value.phoneNumber

                val request = RegisterRequest(
                    username = _uiState.value.username,
                    email = _uiState.value.email,
                    password = _uiState.value.password,
                    address = if (addressValue.isBlank()) null else addressValue,
                    phoneNumber = if (phoneValue.isBlank()) null else phoneValue
                )

                val response = loginRepository.registerUser(request)

                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isRegisterSuccessful = true,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Register gagal: Response kosong"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Register gagal: ${response.message()}"
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

    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}
