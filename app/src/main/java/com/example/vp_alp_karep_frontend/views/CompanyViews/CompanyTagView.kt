package com.example.vp_alp_karep_frontend.views.CompanyViews

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_alp_karep_frontend.models.CompanyModels.CompanyTagsModel
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.CompanyTagStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.CompanyTagViewModel

// Professional color scheme - Balanced for eye comfort
private val PrimaryTeal = Color(0xFF1A4D56)
private val AccentGold = Color(0xFFD4AF37)
private val LightGold = Color(0xFFF0E5C9)
private val DarkTeal = Color(0xFF0F2F35)
private val BackgroundDark = Color(0xFF0A2026)
private val CardBackground = Color(0xFF1E3A41)
private val SecondaryTeal = Color(0xFF2A5F69)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompanyTagView(
    modifier: Modifier = Modifier,
    companyTagViewModel: CompanyTagViewModel,
    context: Context,
    onBackClick: () -> Unit = {}
) {
    val getAllTagsStatus = companyTagViewModel.getAllTagsStatus.collectAsState().value
    val getCompanyProfileStatus = companyTagViewModel.getCompanyProfileStatus.collectAsState().value
    val updateTagsStatus = companyTagViewModel.updateTagsStatus.collectAsState().value
    val selectedTagIds = companyTagViewModel.selectedTagIds

    var currentTags: List<CompanyTagsModel> = emptyList()

    LaunchedEffect(Unit) {
        companyTagViewModel.getAllTags()
        companyTagViewModel.getCompanyProfile()
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
                companyTagViewModel.getCompanyProfile()
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
                        tint = Color.White
                    )
                }
                Text(
                    text = "Manage Company Tags",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
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
                        CircularProgressIndicator(color = AccentGold)
                    }
                }
                getAllTagsStatus is CompanyTagStatusUIState.Failed -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getAllTagsStatus.errorMessage,
                            color = Color(0xFFE57373),
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
                            color = Color(0xFFE57373),
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
                            color = LightGold,
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
                                companyTagViewModel.updateTags(currentTags)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentGold,
                                contentColor = PrimaryTeal,
                                disabledContainerColor = AccentGold.copy(alpha = 0.6f)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            enabled = updateTagsStatus !is StringDataStatusUIState.Loading,
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            if (updateTagsStatus is StringDataStatusUIState.Loading) {
                                CircularProgressIndicator(
                                    color = PrimaryTeal,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 3.dp
                                )
                            } else {
                                Text(
                                    text = "Update Tags",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
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
                color = if (isSelected) AccentGold else CardBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) AccentGold else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = tag.name,
            color = if (isSelected) PrimaryTeal else Color.White,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}