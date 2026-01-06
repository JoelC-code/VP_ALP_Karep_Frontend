package com.example.vp_alp_karep_frontend.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_alp_karep_frontend.uiStates.JobTagStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.JobDetailStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.CreateUpdateJobViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateUpdateJobView(
    modifier: Modifier = Modifier,
    createUpdateJobViewModel: CreateUpdateJobViewModel,
    token: String,
    context: Context,
    onBackClick: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val mode = createUpdateJobViewModel.mode
    val currentJobId = createUpdateJobViewModel.currentJobId
    val getAllJobTagsStatus = createUpdateJobViewModel.getAllJobTagsStatus
    val getJobStatus = createUpdateJobViewModel.getJobStatus
    val createJobStatus = createUpdateJobViewModel.createJobStatus
    val updateJobStatus = createUpdateJobViewModel.updateJobStatus
    val selectedTagIds = createUpdateJobViewModel.selectedTagIds

    var jobName by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isDataLoaded by remember { mutableStateOf(false) }

    // Load tags on start
    LaunchedEffect(Unit) {
        createUpdateJobViewModel.getAllJobTags(token)
    }

    // Reset form when mode or jobId changes
    LaunchedEffect(mode, currentJobId) {
        if (mode == "create") {
            jobName = ""
            jobDescription = ""
            isDataLoaded = true
        } else {
            isDataLoaded = false
            // Reset fields before loading new data
            jobName = ""
            jobDescription = ""
        }
    }

    // Load job data if in update mode
    LaunchedEffect(mode, currentJobId) {
        if (mode == "update" && currentJobId != null) {
            createUpdateJobViewModel.getJob(token, currentJobId)
        }
    }

    // Handle job data loaded for update
    LaunchedEffect(getJobStatus) {
        when (getJobStatus) {
            is JobDetailStatusUIState.Success -> {
                val job = getJobStatus.job
                jobName = job.name
                jobDescription = job.description ?: ""
                // selectedTagIds is already set in ViewModel
                isDataLoaded = true
                createUpdateJobViewModel.clearGetJobStatus()
            }
            is JobDetailStatusUIState.Failed -> {
                Toast.makeText(
                    context,
                    getJobStatus.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                isDataLoaded = true
                createUpdateJobViewModel.clearGetJobStatus()
            }
            else -> {}
        }
    }

    // Handle create job status
    LaunchedEffect(createJobStatus) {
        when (createJobStatus) {
            is JobDetailStatusUIState.Success -> {
                createUpdateJobViewModel.clearCreateJobStatus()
                Toast.makeText(
                    context,
                    "Job berhasil dibuat",
                    Toast.LENGTH_SHORT
                ).show()
                onSuccess()
            }
            is JobDetailStatusUIState.SuccessNoData -> {
                createUpdateJobViewModel.clearCreateJobStatus()
                Toast.makeText(
                    context,
                    "Job berhasil dibuat",
                    Toast.LENGTH_SHORT
                ).show()
                onSuccess()
            }
            is JobDetailStatusUIState.Failed -> {
                isLoading = false
                val errorMsg = createJobStatus.errorMessage
                Toast.makeText(
                    context,
                    if (errorMsg.contains("job_id")) {
                        "Error: Backend database constraint issue. Please check backend logs."
                    } else {
                        errorMsg
                    },
                    Toast.LENGTH_LONG
                ).show()
                createUpdateJobViewModel.clearCreateJobStatus()
            }
            is JobDetailStatusUIState.Loading -> {
                isLoading = true
            }
            else -> {
                isLoading = false
            }
        }
    }

    // Handle update job status
    LaunchedEffect(updateJobStatus) {
        when (updateJobStatus) {
            is JobDetailStatusUIState.Success -> {
                createUpdateJobViewModel.clearUpdateJobStatus()
                Toast.makeText(
                    context,
                    "Job berhasil diupdate",
                    Toast.LENGTH_SHORT
                ).show()
                onSuccess()
            }
            is JobDetailStatusUIState.SuccessNoData -> {
                createUpdateJobViewModel.clearUpdateJobStatus()
                Toast.makeText(
                    context,
                    "Job berhasil diupdate",
                    Toast.LENGTH_SHORT
                ).show()
                onSuccess()
            }
            is JobDetailStatusUIState.Failed -> {
                isLoading = false
                Toast.makeText(
                    context,
                    updateJobStatus.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                createUpdateJobViewModel.clearUpdateJobStatus()
            }
            is JobDetailStatusUIState.Loading -> {
                isLoading = true
            }
            else -> {
                isLoading = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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
                text = if (mode == "create") "Create Job" else "Update Job",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form content
        when (getAllJobTagsStatus) {
            is JobTagStatusUIState.Success -> {
                val availableTags = getAllJobTagsStatus.jobTags

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    // Job Name Field
                    Text(
                        text = "Nama Job",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = jobName,
                        onValueChange = { jobName = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan nama job") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Job Description Field
                    Text(
                        text = "Deskripsi Job",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = jobDescription,
                        onValueChange = { jobDescription = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = { Text("Masukkan deskripsi job") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tags Selection
                    Text(
                        text = "Tags",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableTags.forEach { tag ->
                            val isSelected = selectedTagIds.contains(tag.id)

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (isSelected) Color.Black else Color.White,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) Color.Black else Color.LightGray,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        createUpdateJobViewModel.toggleTagSelection(tag.id)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = tag.name,
                                    fontSize = 14.sp,
                                    color = if (isSelected) Color.White else Color.Black,
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (jobName.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Nama job harus diisi",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (mode == "create") {
                                createUpdateJobViewModel.createJob(
                                    token = token,
                                    name = jobName,
                                    description = jobDescription.ifBlank { null },
                                    tags = selectedTagIds.toList()
                                )
                            } else {
                                currentJobId?.let {
                                    createUpdateJobViewModel.updateJob(
                                        token = token,
                                        jobId = it,
                                        name = jobName,
                                        description = jobDescription.ifBlank { null },
                                        tags = selectedTagIds.toList()
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isLoading) "Loading..." else if (mode == "create") "Create Job" else "Update Job",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            is JobTagStatusUIState.Failed -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getAllJobTagsStatus.errorMessage,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
            else -> {
                Column(
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
}