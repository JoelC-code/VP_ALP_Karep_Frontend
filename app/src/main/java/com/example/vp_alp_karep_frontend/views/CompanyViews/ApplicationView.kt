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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.ApplicationStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.ApplicationViewModel

// Professional color scheme - Balanced for eye comfort
private val PrimaryTeal = Color(0xFF1A4D56)
private val AccentGold = Color(0xFFD4AF37)
private val LightGold = Color(0xFFF0E5C9)
private val DarkTeal = Color(0xFF0F2F35)
private val BackgroundDark = Color(0xFF0A2026)
private val CardBackground = Color(0xFF1E3A41)
private val SecondaryTeal = Color(0xFF2A5F69)

@Composable
fun ApplicationView(
    modifier: Modifier = Modifier,
    applicationViewModel: ApplicationViewModel,
    context: Context,
) {
    val getApplicationsStatus = applicationViewModel.getApplicationsStatus.collectAsState().value
    val acceptApplicationStatus = applicationViewModel.acceptApplicationStatus.collectAsState().value
    val rejectApplicationStatus = applicationViewModel.rejectApplicationStatus.collectAsState().value

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Pending", "Accepted", "Rejected")

    LaunchedEffect(Unit) {
        applicationViewModel.getApplications()
    }

    LaunchedEffect(acceptApplicationStatus) {
        when (acceptApplicationStatus) {
            is StringDataStatusUIState.Success -> {
                applicationViewModel.getApplications()
                applicationViewModel.clearAcceptApplicationStatusErrorMessage()
            }
            is StringDataStatusUIState.Failed -> {
                Toast.makeText(
                    context,
                    acceptApplicationStatus.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                applicationViewModel.clearAcceptApplicationStatusErrorMessage()
            }
            else -> {}
        }
    }

    LaunchedEffect(rejectApplicationStatus) {
        when (rejectApplicationStatus) {
            is StringDataStatusUIState.Success -> {
                applicationViewModel.getApplications()
                applicationViewModel.clearRejectApplicationStatusErrorMessage()
            }
            is StringDataStatusUIState.Failed -> {
                Toast.makeText(
                    context,
                    rejectApplicationStatus.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                applicationViewModel.clearRejectApplicationStatusErrorMessage()
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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Applications",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = CardBackground,
                contentColor = AccentGold
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) AccentGold else Color.White.copy(alpha = 0.7f)
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (getApplicationsStatus) {
                is ApplicationStatusUIState.Success -> {
                    val filteredApplications = getApplicationsStatus.applications.filter {
                        it.status.lowercase().trim() == tabs[selectedTabIndex].lowercase().trim()
                    }

                    if (filteredApplications.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredApplications.size) { index ->
                                ApplicationCard(
                                    applicationModel = filteredApplications[index],
                                    onAccept = {
                                        applicationViewModel.acceptApplication(
                                            applicationId = filteredApplications[index].id
                                        )
                                    },
                                    onReject = {
                                        applicationViewModel.rejectApplication(
                                            applicationId = filteredApplications[index].id
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
                                text = "No ${tabs[selectedTabIndex].lowercase()} applications",
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                is ApplicationStatusUIState.Failed -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getApplicationsStatus.errorMessage,
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