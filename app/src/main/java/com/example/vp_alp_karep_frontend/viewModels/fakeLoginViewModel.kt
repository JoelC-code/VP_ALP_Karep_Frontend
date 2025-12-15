//Hapus jika mau nyambung ke backend beneran
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
import com.example.vp_alp_karep_frontend.AppContainer
import com.example.vp_alp_karep_frontend.repositories.AuthFakeRepository
import com.example.vp_alp_karep_frontend.uiStates.LoginUiStates
import kotlinx.coroutines.launch

class fakeLoginViewModel(
    private val repository: AuthFakeRepository
) : ViewModel() {
    var authStatus: LoginUiStates by mutableStateOf(
        LoginUiStates.Start
    )
        private set

    fun loginUser() {
        authStatus = LoginUiStates.Loading
        viewModelScope.launch {
            try {
                val result = repository.loginUser()
                authStatus = LoginUiStates.Success(result)
            } catch (e: Exception) {
                authStatus = LoginUiStates.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun loginCompany() {
        authStatus = LoginUiStates.Loading
        viewModelScope.launch {
            try {
                val result = repository.loginDev()
                authStatus = LoginUiStates.Success(result)
            } catch (e: Exception) {
                authStatus = LoginUiStates.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun resetState() {
        authStatus = LoginUiStates.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AppContainer)
                val fakeUserRepo = application.container.AuthFakeRepository
                fakeLoginViewModel(fakeUserRepo)
            }
        }
    }
}