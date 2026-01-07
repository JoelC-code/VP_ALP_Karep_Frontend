package com.example.vp_alp_karep_frontend.views

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.vp_alp_karep_frontend.uiStates.ApplicationActionUiState
import com.example.vp_alp_karep_frontend.uiStates.JobDetailUiStates
import com.example.vp_alp_karep_frontend.viewModels.ApplicationActionViewModel
import com.example.vp_alp_karep_frontend.viewModels.ApplicationListViewModel
import com.example.vp_alp_karep_frontend.viewModels.JobDetailViewModel

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

    LaunchedEffect(state) {
        when (state) {
            is ApplicationActionUiState.Success -> {
                viewModel.resetState()
                navController.navigate("main?tab=2"){
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