package com.example.vp_alp_karep_frontend

import android.app.Application

class KarepApplication: Application() {
    lateinit var container: AppCompanyContainer

    override fun onCreate() {
        super.onCreate()
        container = AppCompanyContainer()
    }
}