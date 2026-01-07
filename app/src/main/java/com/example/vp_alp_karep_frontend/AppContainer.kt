package com.example.vp_alp_karep_frontend

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.vp_alp_karep_frontend.repositories.*
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.ApplicationCompanyRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.ApplicationCompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyTagRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyTagRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.JobCompanyRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.JobCompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.NotificationRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.NotificationRepositoryInterface
import com.example.vp_alp_karep_frontend.service.*
import com.example.vp_alp_karep_frontend.service.CompanyService.ApplicationService
import com.example.vp_alp_karep_frontend.service.CompanyService.CompanyService
import com.example.vp_alp_karep_frontend.service.CompanyService.CompanyTagService
import com.example.vp_alp_karep_frontend.service.CompanyService.JobCompanyService
import com.example.vp_alp_karep_frontend.service.CompanyService.NotificationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainerInterface {
    val authRepository: AuthRepositoryInterface
    val jobRepository: JobRepositoryInterface
    val applicationRepository: ApplicationRepositoryInterface
    val companyRepository: CompanyRepositoryInterface
    val notificationRepository: NotificationRepositoryInterface
    val applicationCompanyRepository: ApplicationCompanyRepositoryInterface
    val jobCompanyRepository: JobCompanyRepositoryInterface
    val companyTagCompanyRepository: CompanyTagRepositoryInterface
}

class AppContainer(
    private val dataStore: DataStore<Preferences>
): AppContainerInterface {
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

    private val AuthAPI: AuthFakeAPI by lazy {
        val retrofit = initRetrofit()
        retrofit.create(AuthFakeAPI::class.java)
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

    override val jobRepository: JobRepositoryInterface by lazy {
        JobRepository(JobAPI)
    }

    override val applicationRepository: ApplicationRepositoryInterface by lazy {
        ApplicationRepository(ApplicationAPI)
    }

    // Company Repositories
    private val companyRetrofitService: CompanyService by lazy {
        val  retrofit = initRetrofit()

        retrofit.create(CompanyService::class.java)
    }

    private val notificationRetrofitService: NotificationService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(NotificationService::class.java)
    }

    private val applicationRetrofitService: ApplicationService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(ApplicationService::class.java)
    }

    private val jobRetrofitService: JobCompanyService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(JobCompanyService::class.java)
    }

    private val companyTagRetrofitService: CompanyTagService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(CompanyTagService::class.java)
    }

    override val companyRepository: CompanyRepositoryInterface by lazy {
        CompanyRepository(companyRetrofitService)
    }

    override val notificationRepository: NotificationRepositoryInterface by lazy {
        NotificationRepository(notificationRetrofitService)
    }

    override val applicationCompanyRepository: ApplicationCompanyRepositoryInterface by lazy {
        ApplicationCompanyRepository(applicationRetrofitService)
    }

    override val jobCompanyRepository: JobCompanyRepositoryInterface by lazy {
        JobCompanyRepository(jobRetrofitService)
    }

    override val companyTagCompanyRepository: CompanyTagRepositoryInterface by lazy {
        CompanyTagRepository(companyTagRetrofitService)
    }
}
