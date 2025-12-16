//TODO Hapus jika mau nyambung ke backend beneran
package com.example.vp_alp_karep_frontend.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.models.LoginFakeResponse
import com.example.vp_alp_karep_frontend.repositories.AuthFakeRepository
import com.example.vp_alp_karep_frontend.repositories.AuthRepositoryInterface
import com.example.vp_alp_karep_frontend.uiStates.LoginUiStates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FakeLoginViewModel(
    private val repository: AuthRepositoryInterface
) : ViewModel() {

    var cachedToken: String? = null

    var canApply: Boolean = true

    var authStatus: LoginUiStates by mutableStateOf(
        LoginUiStates.Start
    )
        private set

    fun loginUser() {
        authStatus = LoginUiStates.Loading

        try {
            val call = repository.loginUser()

            call.enqueue(object: Callback<LoginFakeResponse> {
                override fun onResponse(
                    call: Call<LoginFakeResponse>,
                    response: Response<LoginFakeResponse>
                ) {
                    if(response.isSuccessful && response.body() != null) {
                        authStatus = LoginUiStates.Success(response.body()!!)
                        canApply = true
                    } else {
                        authStatus = LoginUiStates.Error("Login failed")
                    }
                }

                override fun onFailure(p0: Call<LoginFakeResponse?>, t: Throwable) {
                    authStatus = LoginUiStates.Error(t.localizedMessage!!)
                }
            })
        } catch (e: Exception) {
            authStatus = LoginUiStates.Error(e.localizedMessage!!)
        }
    }

    fun loginCompany() {
        authStatus = LoginUiStates.Loading

        try {
            val call = repository.loginDev()

            call.enqueue(object: Callback<LoginFakeResponse> {
                override fun onResponse(
                    call: Call<LoginFakeResponse>,
                    response: Response<LoginFakeResponse>
                ) {
                    if(response.isSuccessful && response.body() != null) {
                        authStatus = LoginUiStates.Success(response.body()!!)
                        canApply = false
                    } else {
                        authStatus = LoginUiStates.Error("Login failed")
                    }
                }

                override fun onFailure(p0: Call<LoginFakeResponse?>, t: Throwable) {
                    authStatus = LoginUiStates.Error(t.localizedMessage!!)
                }
            })
        } catch (e: Exception) {
            authStatus = LoginUiStates.Error(e.localizedMessage!!)
        }
    }

    fun resetState() {
        authStatus = LoginUiStates.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as KarepApplication
                val repo = app.container.authRepository
                FakeLoginViewModel(repo)
            }
        }
    }
}