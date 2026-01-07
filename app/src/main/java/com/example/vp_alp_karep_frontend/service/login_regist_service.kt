package com.example.vp_alp_karep_frontend.service

import com.example.vp_alp_karep_frontend.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRegistService {

    @POST("api/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("api/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("api/register/user")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("api/register/company")
    suspend fun registerCompany(
        @Body companyRegisterRequest: CompanyRegisterRequest
    ): Response<CompanyRegisterResponse>
}