package com.example.vp_alp_karep_frontend.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_alp_karep_frontend.uiStates.JobStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.StringDataStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.CreateUpdateJobViewModel
import com.example.vp_alp_karep_frontend.viewModels.JobViewModel

@Composable
fun JobView(
    modifier: Modifier = Modifier,
    jobViewModel: JobViewModel,
    createUpdateJobViewModel: CreateUpdateJobViewModel,
    token: String,
    context: Context,
    onBackClick: () -> Unit = {},
    onNavigateToCreateUpdate: () -> Unit = {}
) {
    val getAllJobsStatus = jobViewModel.getAllJobsStatus
    val deleteJobStatus = jobViewModel.deleteJobStatus

    LaunchedEffect(token) {
        jobViewModel.getAllJobsByCompany(token)
    }

    LaunchedEffect(deleteJobStatus) {
        when (deleteJobStatus) {
            is StringDataStatusUIState.Success -> {
                Toast.makeText(
                    context,
                    "Job berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
                jobViewModel.getAllJobsByCompany(token)
                jobViewModel.clearDeleteJobErrorMessage()
            }
            is StringDataStatusUIState.Failed -> {
                Toast.makeText(
                    context,
                    deleteJobStatus.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                jobViewModel.clearDeleteJobErrorMessage()
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Job Positions",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Button(
                onClick = {
                    createUpdateJobViewModel.setMode("create")
                    onNavigateToCreateUpdate()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create job",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "Create",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when(getAllJobsStatus) {
            is JobStatusUIState.Success -> if (getAllJobsStatus.jobs.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(getAllJobsStatus.jobs) { job ->
                        JobCard(
                            jobModel = job,
                            onDelete = {
                                jobViewModel.deleteJob(token, job.id)
                            },
                            onUpdate = {
                                createUpdateJobViewModel.setMode("update", job.id)
                                onNavigateToCreateUpdate()
                            }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada job position",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
            is JobStatusUIState.Failed -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getAllJobsStatus.errorMessage,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            else -> Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircleLoadingTemplate(
                    color = Color(0xFF4CAF50),
                    trackColor = Color.Transparent
                )
            }
        }
    }
}