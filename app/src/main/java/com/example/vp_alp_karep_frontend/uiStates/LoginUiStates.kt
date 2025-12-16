//TODO Hapus jika udah nyambung
package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.LoginFakeResponse

sealed class LoginUiStates {
    object Start : LoginUiStates()
    object Loading : LoginUiStates()
    data class Success(val result: LoginFakeResponse) : LoginUiStates()
    data class Error(val message: String) : LoginUiStates()
}