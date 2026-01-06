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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.ApplicationStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.ApplicationViewModel

@Composable
fun ApplicationView(
    modifier: Modifier = Modifier,
    applicationViewModel: ApplicationViewModel,
    token: String,
    context: Context,
    onBackClick: () -> Unit = {}
) {
    val getApplicationsStatus = applicationViewModel.getApplicationsStatus
    val acceptApplicationStatus = applicationViewModel.acceptApplicationStatus
    val rejectApplicationStatus = applicationViewModel.rejectApplicationStatus

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Pending", "Accepted", "Rejected")

    LaunchedEffect(token) {
        applicationViewModel.getApplications(token)
    }

    LaunchedEffect(acceptApplicationStatus) {
        when (acceptApplicationStatus) {
            is StringDataStatusUIState.Success -> {
                applicationViewModel.getApplications(token)
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
                applicationViewModel.getApplications(token)
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
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Applications",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = Color(0xFF2196F3)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
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
                                        token = token,
                                        applicationId = filteredApplications[index].id
                                    )
                                },
                                onReject = {
                                    applicationViewModel.rejectApplication(
                                        token = token,
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
                            color = Color.Gray
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
                    color = Color.Gray
                )
            }
            else -> Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircleLoadingTemplate(
                    color = Color(0xFF2196F3),
                    trackColor = Color.Transparent
                )
            }
        }
    }
}