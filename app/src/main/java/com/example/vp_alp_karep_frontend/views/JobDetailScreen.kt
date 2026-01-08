package com.example.vp_alp_karep_frontend.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.vp_alp_karep_frontend.uiStates.ApplicationActionUiState
import com.example.vp_alp_karep_frontend.uiStates.JobDetailUiStates
import com.example.vp_alp_karep_frontend.viewModels.ApplicationActionViewModel
import com.example.vp_alp_karep_frontend.viewModels.ApplicationListViewModel
import com.example.vp_alp_karep_frontend.viewModels.JobDetailViewModel

private val PrimaryTeal = Color(0xFF1A4D56)      // Balanced teal
private val AccentGold = Color(0xFFD4AF37)       // Brighter gold
private val LightGold = Color(0xFFF0E5C9)        // Softer light gold
private val DarkTeal = Color(0xFF0F2F35)         // Soft dark teal
private val BackgroundDark = Color(0xFF0A2026)   // Comfortable dark background
private val CardBackground = Color(0xFF1E3A41)   // Balanced card background
private val SecondaryTeal = Color(0xFF2A5F69)

@Composable
fun JobDetailScreen(
    jobId: Int,
    navController: NavHostController,
    viewModel: JobDetailViewModel = viewModel(factory = JobDetailViewModel.Factory),
    viewModelApp: ApplicationActionViewModel = viewModel(factory = ApplicationActionViewModel.Factory)
) {
    val context = LocalContext.current
    val state by viewModelApp.applyState.collectAsState()
    val jobState by viewModel.uiStates.collectAsState()

    LaunchedEffect(jobId) {
        viewModel.loadJobDetail(jobId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    when(jobState) {
        is JobDetailUiStates.start -> {}
        is JobDetailUiStates.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is JobDetailUiStates.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text((jobState as JobDetailUiStates.Error).message)
            }
        }
        is JobDetailUiStates.Success -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                BackgroundDark,
                                DarkTeal,
                                PrimaryTeal.copy(alpha = 0.4f),
                                SecondaryTeal.copy(alpha = 0.2f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            ) {
                JobDetailContent(
                    job = (jobState as JobDetailUiStates.Success).job,
                    onApplyClick = {
                        viewModelApp.applyJob(jobId)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

    LaunchedEffect(state) {
        when (state) {
            is ApplicationActionUiState.Success -> {
                viewModel.resetState()
                navController.navigate("main?tab=1"){
                    popUpTo("main") {inclusive = false}
                    launchSingleTop = true
                }
            }

            is ApplicationActionUiState.Error -> {
                Toast.makeText(
                    context,
                    "Cannot apply to this job: ${(state as ApplicationActionUiState.Error).message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }
}