package com.example.vp_alp_karep_frontend

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.vp_alp_karep_frontend.repositories.ApplicationRepository
import com.example.vp_alp_karep_frontend.repositories.ApplicationRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyTagRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyTagRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.JobRepository
import com.example.vp_alp_karep_frontend.repositories.JobRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.NotificationRepository
import com.example.vp_alp_karep_frontend.repositories.NotificationRepositoryInterface
import com.example.vp_alp_karep_frontend.service.ApplicationService
import com.example.vp_alp_karep_frontend.service.CompanyService
import com.example.vp_alp_karep_frontend.service.CompanyTagService
import com.example.vp_alp_karep_frontend.service.NotificationService
import com.example.vp_alp_karep_frontend.service.JobService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainerInterface {
    val companyRepository: CompanyRepositoryInterface
    val notificationRepository: NotificationRepositoryInterface
    val applicationRepository: ApplicationRepositoryInterface
    val jobRepository: JobRepositoryInterface
    val companyTagRepository: CompanyTagRepositoryInterface
}

class AppContainer (
): AppContainerInterface {
//    private val backendURL = "http://192.168.56.1:3000/"
//    private val backendURL = "http://10.118.177.225:3000/"
//    private val backendURL = "http://10.0.40.131:3000/"
private  val backendURL = "http://10.0.162.147:3000/"
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

    private val jobRetrofitService: JobService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(JobService::class.java)
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

    override val applicationRepository: ApplicationRepositoryInterface by lazy {
        ApplicationRepository(applicationRetrofitService)
    }

    override val jobRepository: JobRepositoryInterface by lazy {
        JobRepository(jobRetrofitService)
    }

    override val companyTagRepository: CompanyTagRepositoryInterface by lazy {
        CompanyTagRepository(companyTagRetrofitService)
    }

    private fun initRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)

        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client.build()).baseUrl(backendURL).build()
    }
}