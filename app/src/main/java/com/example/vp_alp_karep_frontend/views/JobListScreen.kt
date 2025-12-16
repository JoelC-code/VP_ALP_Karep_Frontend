package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.vp_alp_karep_frontend.enums.JobListStatus
import com.example.vp_alp_karep_frontend.viewModels.JobListViewModel

@Composable
fun JobListScreen(
    token: String,
    navController: NavHostController,
    startMode: JobListStatus,
    viewModel: JobListViewModel = viewModel(factory = JobListViewModel.Factory)
) {
    var mode by remember { mutableStateOf(startMode) }
    val state by viewModel.uiStates.collectAsState()

    LaunchedEffect(mode, token) {
        when(mode) {
            JobListStatus.ALL -> viewModel.loadJobsAll(token)
            JobListStatus.COMPANY -> viewModel.loadJobsOfCompany(token)
            JobListStatus.SEARCH -> Unit
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    Column {
        if(mode != JobListStatus.COMPANY) {
            JobTopBar(
                onSearch = { search ->
                    mode = JobListStatus.SEARCH
                    viewModel.loadJobsOnSearch(token, search)
                },
                onShowAll = {
                    viewModel.loadJobsAll(token)
                }
            )
        }

        JobContent(state, onJobClick = { jobId -> navController.navigate("job_detail/$jobId")})
    }
}