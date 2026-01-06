package com.example.vp_alp_karep_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.vp_alp_karep_frontend.ui.theme.VP_ALP_Karep_FrontendTheme
import com.example.vp_alp_karep_frontend.views.CompanyViews.KarepMain

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VP_ALP_Karep_FrontendTheme {
                KarepMain()
            }
        }
    }
}