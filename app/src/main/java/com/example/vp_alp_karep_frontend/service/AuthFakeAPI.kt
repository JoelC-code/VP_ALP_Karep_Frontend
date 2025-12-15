//Hapus jika mau nyambung ke backend beneran
package com.example.vp_alp_karep_frontend.service

import com.example.vp_alp_karep_frontend.models.LoginFakeResponse
import retrofit2.Call
import retrofit2.http.POST

interface AuthFakeAPI {
    @POST("api/loginDev")
    fun loginDev(): Call<LoginFakeResponse>

    @POST("api/loginUser")
    fun loginUser(): Call<LoginFakeResponse>
}