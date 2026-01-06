package com.example.vp_alp_karep_frontend

import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.ApplicationCompanyRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.ApplicationCompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyTagRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.CompanyTagRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.JobCompanyRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.JobRepositoryInterface
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.NotificationRepository
import com.example.vp_alp_karep_frontend.repositories.CompanyRepository.NotificationRepositoryInterface
import com.example.vp_alp_karep_frontend.service.CompanyService.ApplicationService
import com.example.vp_alp_karep_frontend.service.CompanyService.CompanyService
import com.example.vp_alp_karep_frontend.service.CompanyService.CompanyTagService
import com.example.vp_alp_karep_frontend.service.CompanyService.NotificationService
import com.example.vp_alp_karep_frontend.service.CompanyService.JobCompanyService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppCompanyContainerInterface {
    val companyRepository: CompanyRepositoryInterface
    val notificationRepository: NotificationRepositoryInterface
    val applicationRepository: ApplicationCompanyRepositoryInterface
    val jobRepository: JobRepositoryInterface
    val companyTagRepository: CompanyTagRepositoryInterface
}

class AppCompanyContainer (
): AppCompanyContainerInterface {
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

    override val applicationRepository: ApplicationCompanyRepositoryInterface by lazy {
        ApplicationCompanyRepository(applicationRetrofitService)
    }

    override val jobRepository: JobRepositoryInterface by lazy {
        JobCompanyRepository(jobRetrofitService)
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