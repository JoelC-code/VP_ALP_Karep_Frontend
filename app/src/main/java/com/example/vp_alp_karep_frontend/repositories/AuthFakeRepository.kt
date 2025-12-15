//Hapus jika mau nyambung ke backend beneran
package com.example.vp_alp_karep_frontend.repositories

import com.example.vp_alp_karep_frontend.service.AuthFakeAPI

class AuthFakeRepository(
    private val api: AuthFakeAPI
) {
    suspend fun loginDev() = api.loginDev()

    suspend fun loginUser() = api.loginUser()
}