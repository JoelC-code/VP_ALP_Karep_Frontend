package com.example.vp_alp_karep_frontend.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vp_alp_karep_frontend.uiStates.JobListUiStates
import com.example.vp_alp_karep_frontend.views.templates.JobCard

@Composable
fun JobContent(state: JobListUiStates, onJobClick: (Int) -> Unit ) {
    when(state) {
        is JobListUiStates.Start -> {}
        is JobListUiStates.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is JobListUiStates.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
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
        }

        is JobListUiStates.Success -> {
            Log.d("JOB_DETAIL", "jobId = $onJobClick")
            JobCard(jobs = state.jobs, onJobClick = { job -> onJobClick(job)})
        }
    }
}