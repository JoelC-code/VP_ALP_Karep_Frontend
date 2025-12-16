package com.example.vp_alp_karep_frontend.uiStates

sealed class ApplicationActionUiState {
    object Idle : ApplicationActionUiState()
    object Loading : ApplicationActionUiState()
    object Success : ApplicationActionUiState()
    data class Error(val message: String) : ApplicationActionUiState()
}