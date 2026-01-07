package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_alp_karep_frontend.models.LoginRequest
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.uiStates.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRegistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun login() {
        if (_uiState.value.username.isBlank() || _uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Email dan password tidak boleh kosong"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val response = loginRepository.login(
                    LoginRequest(
                        email = _uiState.value.username,
                        password = _uiState.value.password
                    )
                )

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.data?.token != null) {
                        // Save token
                        loginRepository.saveAuthToken(loginResponse.data.token)
                        loginRepository.saveUserInfo(
                            userId = "0",
                            username = _uiState.value.username
                        )

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoginSuccessful = true,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Login gagal: Response kosong"
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Login gagal: ${errorBody ?: response.message()}"
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
        _uiState.value = LoginUiState()
    }
}
