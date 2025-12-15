package com.example.vp_alp_karep_frontend

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.vp_alp_karep_frontend.repositories.ApplicationRepository
import com.example.vp_alp_karep_frontend.repositories.ApplicationRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.AuthFakeRepository
import com.example.vp_alp_karep_frontend.repositories.AuthRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.JobRepository
import com.example.vp_alp_karep_frontend.repositories.JobRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.JobTagRepository
import com.example.vp_alp_karep_frontend.repositories.JobTagRepositoryInterface
import com.example.vp_alp_karep_frontend.service.ApplicationServices
import com.example.vp_alp_karep_frontend.service.AuthFakeAPI
import com.example.vp_alp_karep_frontend.service.JobServices
import com.example.vp_alp_karep_frontend.service.JobTagService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainerInterface {
    val authRepository: AuthRepositoryInterface
    val jobRepository: JobRepositoryInterface
    val jobTagRepository: JobTagRepositoryInterface
    val applicationRepository: ApplicationRepositoryInterface
}

class AppContainer (
    private val dataStore: DataStore<Preferences>
): AppContainerInterface {
    /*IPaddress di cari dengan cara
        1. Buka cmd
        2. Ketik "ipconfig"
        3. Cari "IPv4 Address" pada bagian koneksi yang digunakan (Wi-Fi)
        4. Ganti "192.168.x.xx" pada backendURL dengan IPv4 Address yang ditemukan
        -- IP Address UC bisa beda beda jadi cari sendiri dan ganti sebelum presentasi! --
     */
    private val backendURL = "http://192.168.x.xx:3000"


    //RETROFIT SERVICE
    //Calling Service
    private val AuthAPI: AuthFakeAPI by lazy {
        val retrofit = initRetrofit()
        retrofit.create(AuthFakeAPI::class.java)
    }

    private val JobTagAPI: JobTagService by lazy {
        val retrofit = initRetrofit()
        retrofit.create(JobTagService::class.java)
    }

    private val JobAPI: JobServices by lazy {
        val retrofit = initRetrofit()
        retrofit.create(JobServices::class.java)
    }

    private val ApplicationAPI: ApplicationServices by lazy {
        val retrofit = initRetrofit()
        retrofit.create(ApplicationServices::class.java)
    }

    //RETROFIT INIT
    //TO Repository
    override val authRepository: AuthRepositoryInterface by lazy {
        AuthFakeRepository(AuthAPI)
    }

    override val jobTagRepository: JobTagRepositoryInterface by lazy {
        JobTagRepository(JobTagAPI)
    }

    override val jobRepository: JobRepositoryInterface by lazy {
        JobRepository(JobAPI)
    }

    override val applicationRepository: ApplicationRepositoryInterface by lazy {
        ApplicationRepository(ApplicationAPI)
    }

    //Init Retrofit + AuthInterceptor untuk ngambil token dari DataStore
    private fun initRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)

        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client.build()).baseUrl(backendURL).build()
    }
}