package com.example.vp_alp_karep_frontend

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.vp_alp_karep_frontend.interceptors.AuthInterceptor
import com.example.vp_alp_karep_frontend.service.AuthFakeAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer (
    private val dataStore: DataStore<Preferences>
) {
    /*IPaddress di cari dengan cara
        1. Buka cmd
        2. Ketik "ipconfig"
        3. Cari "IPv4 Address" pada bagian koneksi yang digunakan (Wi-Fi)
        4. Ganti "192.168.x.xx" pada backendURL dengan IPv4 Address yang ditemukan
        -- IP Address UC bisa beda beda jadi cari sendiri dan ganti sebelum presentasi! --
     */
    private val backendURL = "http://192.168.x.xx:3000"


    //RETROFIT SERVICE
    private val authRetrofitService: AuthFakeAPI by lazy {
        val retrofit = initRetrofit()
        retrofit.create(AuthFakeAPI::class.java)
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