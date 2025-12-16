//TODO Hapus jika mau nyambung ke backend beneran
package com.example.vp_alp_karep_frontend.repositories

import com.example.vp_alp_karep_frontend.models.LoginFakeResponse
import com.example.vp_alp_karep_frontend.service.AuthFakeAPI
import retrofit2.Call

interface AuthRepositoryInterface {
    fun loginDev(): Call<LoginFakeResponse>
    fun loginUser(): Call<LoginFakeResponse>
}

class AuthFakeRepository(
    private val api: AuthFakeAPI
): AuthRepositoryInterface {
    override fun loginDev(): Call<LoginFakeResponse> {
        return api.loginDev()
    }

    override fun loginUser(): Call<LoginFakeResponse> {
        return api.loginUser()
    }
}