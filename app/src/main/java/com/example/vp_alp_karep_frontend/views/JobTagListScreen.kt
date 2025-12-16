package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.vp_alp_karep_frontend.uiStates.JobTagUiStates
import com.example.vp_alp_karep_frontend.viewModels.JobTagListViewModel
import com.example.vp_alp_karep_frontend.views.templates.JobTagChecklist

@Composable
fun JobTagListScreen(
    token: String,
    viewModel: JobTagListViewModel = viewModel(factory = JobTagListViewModel.Factory)
) {
    val getJobTagStatus by viewModel.uiStates.collectAsState()

    LaunchedEffect(token) {
        viewModel.loadJobTags(token)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    when (val state = getJobTagStatus) {
        is JobTagUiStates.Start -> {/* no reactive */}
        is JobTagUiStates.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Loading...")
                }
            }
        }
        is JobTagUiStates.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = (state.message)
                    )
                }
            }
        }
        is JobTagUiStates.Success -> {
            JobTagChecklist(state.tags)
        }
    }
}