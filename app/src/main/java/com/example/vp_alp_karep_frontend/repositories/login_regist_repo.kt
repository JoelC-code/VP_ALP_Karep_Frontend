package com.example.vp_alp_karep_frontend.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vp_alp_karep_frontend.models.*
import com.example.vp_alp_karep_frontend.service.LoginRegistService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

interface LoginRegistRepositoryInterface {
    suspend fun saveAuthToken(token: String)
    suspend fun saveUserInfo(userId: String, username: String)
    suspend fun saveAccountType(accountType: String)
    fun getAuthToken(): Flow<String?>
    fun getUserId(): Flow<String?>
    fun getUsername(): Flow<String?>
    fun getAccountType(): Flow<String?>
    suspend fun clearAuthData()
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse>
    suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse>
    suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse>
    suspend fun registerCompany(companyRegisterRequest: CompanyRegisterRequest): Response<CompanyRegisterResponse>
}

class LoginRegistRepository(
    private val loginRegistService: LoginRegistService,
    private val dataStore: DataStore<Preferences>
) : LoginRegistRepositoryInterface {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val ACCOUNT_TYPE_KEY = stringPreferencesKey("account_type")
    }

    // DataStore operations
    override suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun saveUserInfo(userId: String, username: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USERNAME_KEY] = username
        }
    }

    override suspend fun saveAccountType(accountType: String) {
        dataStore.edit { preferences ->
            preferences[ACCOUNT_TYPE_KEY] = accountType
        }
    }

    override fun getAuthToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    override fun getUserId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }

    override fun getUsername(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USERNAME_KEY]
        }
    }

    override fun getAccountType(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[ACCOUNT_TYPE_KEY]
        }
    }

    override suspend fun clearAuthData() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
            preferences.remove(USERNAME_KEY)
            preferences.remove(ACCOUNT_TYPE_KEY)
        }
    }

    // API calls
    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return loginRegistService.login(loginRequest)
    }

    override suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> {
        return loginRegistService.register(registerRequest)
    }

    override suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse> {
        return loginRegistService.registerUser(registerRequest)
    }

    override suspend fun registerCompany(companyRegisterRequest: CompanyRegisterRequest): Response<CompanyRegisterResponse> {
        return loginRegistService.registerCompany(companyRegisterRequest)
    }
}