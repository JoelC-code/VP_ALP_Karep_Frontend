package com.example.vp_alp_karep_frontend.views.CompanyViews

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vp_alp_karep_frontend.navigation.Screen
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.JobStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.JobViewModel

// Professional color scheme - Balanced for eye comfort
private val PrimaryTeal = Color(0xFF1A4D56)
private val AccentGold = Color(0xFFD4AF37)
private val LightGold = Color(0xFFF0E5C9)
private val DarkTeal = Color(0xFF0F2F35)
private val BackgroundDark = Color(0xFF0A2026)
private val CardBackground = Color(0xFF1E3A41)
private val SecondaryTeal = Color(0xFF2A5F69)

@Composable
fun JobView(
    modifier: Modifier = Modifier,
    jobViewModel: JobViewModel,
    context: Context,
    navController: NavHostController
) {
    val getAllJobsStatus = jobViewModel.getAllJobsStatus.collectAsState().value
    val deleteJobStatus = jobViewModel.deleteJobStatus.collectAsState().value

    LaunchedEffect(Unit) {
        jobViewModel.getAllJobsByCompany()
    }

    LaunchedEffect(deleteJobStatus) {
        when (deleteJobStatus) {
            is StringDataStatusUIState.Success -> {
                Toast.makeText(
                    context,
                    "Job berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
                jobViewModel.getAllJobsByCompany()
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

    Box(
        modifier = modifier
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
        Column(
            modifier = Modifier
                .fillMaxSize()
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


                    Text(
                        text = "Job Positions",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        navController.navigate(Screen.CreateUpdateJob.createRoute(mode = "create"))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentGold,
                        contentColor = PrimaryTeal
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create job",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = "Create",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (getAllJobsStatus) {
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
                                    jobViewModel.deleteJob(job.id)
                                },
                                onUpdate = {
                                    navController.navigate(
                                        Screen.CreateUpdateJob.createRoute(
                                            mode = "update",
                                            jobId = job.id
                                        )
                                    )
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
                            color = Color.White.copy(alpha = 0.7f)
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
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                else -> Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircleLoadingTemplate(
                        color = AccentGold,
                        trackColor = Color.Transparent
                    )
                }
            }
        }
    }
}