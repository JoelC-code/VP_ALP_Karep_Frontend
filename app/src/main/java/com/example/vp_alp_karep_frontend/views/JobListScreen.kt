package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.vp_alp_karep_frontend.enums.JobListStatus
import com.example.vp_alp_karep_frontend.viewModels.JobListViewModel

private val PrimaryTeal = Color(0xFF1A4D56)      // Balanced teal
private val AccentGold = Color(0xFFD4AF37)       // Brighter gold
private val LightGold = Color(0xFFF0E5C9)        // Softer light gold
private val DarkTeal = Color(0xFF0F2F35)         // Soft dark teal
private val BackgroundDark = Color(0xFF0A2026)   // Comfortable dark background
private val CardBackground = Color(0xFF1E3A41)   // Balanced card background
private val SecondaryTeal = Color(0xFF2A5F69)

@Composable
fun JobListScreen(
    navController: NavHostController,
    startMode: JobListStatus,
    viewModel: JobListViewModel = viewModel(factory = JobListViewModel.Factory)
) {
    var mode by remember { mutableStateOf(startMode) }
    val state by viewModel.uiStates.collectAsState()

    LaunchedEffect(mode) {
        when(mode) {
            JobListStatus.ALL -> viewModel.loadJobsAll()
            JobListStatus.COMPANY -> viewModel.loadJobsOfCompany()
            JobListStatus.SEARCH -> Unit
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    Column {
        if (mode != JobListStatus.COMPANY) {
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
                Column {
                    JobTopBar(
                        onSearch = { search ->
                            mode = JobListStatus.SEARCH
                            viewModel.loadJobsOnSearch(search)
                        },
                        onShowAll = {
                            viewModel.loadJobsAll()
                        }
                    )
                    JobContent(
                        state,
                        onJobClick = { jobId -> navController.navigate("job_detail/$jobId") })

                }
            }
        }
    }
}