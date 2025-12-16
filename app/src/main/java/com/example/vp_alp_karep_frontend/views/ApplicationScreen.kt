package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vp_alp_karep_frontend.viewModels.ApplicationListViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vp_alp_karep_frontend.uiStates.ApplicationListUiStates
import com.example.vp_alp_karep_frontend.views.templates.ApplicationCards

@Composable
fun ApplicationScreen(
    token: String,
    viewModel: ApplicationListViewModel = viewModel(factory = ApplicationListViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(token) {
        viewModel.loadApplications(token)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    when(state) {
        is ApplicationListUiStates.Start -> {}
        is ApplicationListUiStates.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ApplicationListUiStates.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text((state as ApplicationListUiStates.Error).message)
            }
        }
        is ApplicationListUiStates.Success -> {
            ApplicationCards(
                onCancel = { id ->
                    viewModel.cancelApplication(token, id)
                },
                onDelete = { id ->
                    viewModel.deleteApplication(token, id)
                },
                applications = (state as ApplicationListUiStates.Success).applications
            )
        }
    }
}