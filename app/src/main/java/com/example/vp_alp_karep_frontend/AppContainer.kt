package com.example.vp_alp_karep_frontend

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.vp_alp_karep_frontend.repositories.*
import com.example.vp_alp_karep_frontend.service.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(
    private val dataStore: DataStore<Preferences>
) {
    /*IPaddress di cari dengan cara
        1. Buka cmd
        2. Ketik "ipconfig"
        3. Cari "IPv4 Address" pada bagian koneksi yang digunakan (Wi-Fi)
        4. Ganti "192.168.x.xx" pada backendURL dengan IPv4 Address yang ditemukan
        -- IP Address UC bisa beda beda jadi cari sendiri dan ganti sebelum presentasi! --
     */
    private val backendURL = "http://10.0.163.64:3000/"

    private fun initRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(backendURL)
            .build()
    }

    // Services
    private val retrofit: Retrofit by lazy { initRetrofit() }

    private val loginRegistService: LoginRegistService by lazy {
        retrofit.create(LoginRegistService::class.java)
    }

    private val userProfileService: UserProfileService by lazy {
        retrofit.create(UserProfileService::class.java)
    }

    private val userExperienceService: UserExperienceService by lazy {
        retrofit.create(UserExperienceService::class.java)
    }

    private val userAchievementService: UserAchievementService by lazy {
        retrofit.create(UserAchievementService::class.java)
    }

    // Repositories
    val loginRegistRepository: LoginRegistRepository by lazy {
        LoginRegistRepository(loginRegistService, dataStore)
    }

    val userProfileRepository: UserProfileRepository by lazy {
        UserProfileRepository(userProfileService)
    }

    val userExperienceRepository: UserExperienceRepository by lazy {
        UserExperienceRepository(userExperienceService)
    }

    val userAchievementRepository: UserAchievementRepository by lazy {
        UserAchievementRepository(userAchievementService)
    }
}
