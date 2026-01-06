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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.vp_alp_karep_frontend.models.CompanyTagsModel
import com.example.vp_alp_karep_frontend.uiStates.CompanyTagStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.StringDataStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.CompanyTagViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompanyTagView(
    modifier: Modifier = Modifier,
    companyTagViewModel: CompanyTagViewModel,
    token: String,
    context: Context,
    onBackClick: () -> Unit = {}
) {
    val getAllTagsStatus = companyTagViewModel.getAllTagsStatus
    val getCompanyProfileStatus = companyTagViewModel.getCompanyProfileStatus
    val updateTagsStatus = companyTagViewModel.updateTagsStatus
    val selectedTagIds = companyTagViewModel.selectedTagIds

    var currentTags: List<CompanyTagsModel> = emptyList()

    LaunchedEffect(Unit) {
        companyTagViewModel.getAllTags(token)
        companyTagViewModel.getCompanyProfile(token)
    }

    LaunchedEffect(updateTagsStatus) {
        when (updateTagsStatus) {
            is StringDataStatusUIState.Success -> {
                Toast.makeText(
                    context,
                    "Tags updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
                companyTagViewModel.clearUpdateTagsStatus()
                companyTagViewModel.getCompanyProfile(token)
            }
            is StringDataStatusUIState.Failed -> {
                Toast.makeText(
                    context,
                    updateTagsStatus.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                companyTagViewModel.clearUpdateTagsStatus()
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
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "Manage Company Tags",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Content
        when {
            getAllTagsStatus is CompanyTagStatusUIState.Loading ||
            getCompanyProfileStatus is CompanyTagStatusUIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }
            getAllTagsStatus is CompanyTagStatusUIState.Failed -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getAllTagsStatus.errorMessage,
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }
            }
            getCompanyProfileStatus is CompanyTagStatusUIState.Failed -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getCompanyProfileStatus.errorMessage,
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }
            }
            getAllTagsStatus is CompanyTagStatusUIState.Success &&
            getCompanyProfileStatus is CompanyTagStatusUIState.Success -> {
                val allTags = getAllTagsStatus.tags
                currentTags = getCompanyProfileStatus.tags

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Select tags for your company:",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Tags grid
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allTags.forEach { tag ->
                            val isSelected = selectedTagIds.contains(tag.id)
                            TagChip(
                                tag = tag,
                                isSelected = isSelected,
                                onClick = {
                                    companyTagViewModel.toggleTagSelection(tag.id)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Update button
                    Button(
                        onClick = {
                            companyTagViewModel.updateTags(token, currentTags)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = updateTagsStatus !is StringDataStatusUIState.Loading
                    ) {
                        if (updateTagsStatus is StringDataStatusUIState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Update Tags",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun TagChip(
    tag: CompanyTagsModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) Color.Black else Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = tag.name,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}