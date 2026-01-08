package com.example.vp_alp_karep_frontend.views.CompanyViews

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.CompanyStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.StringDataStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.UpdateCompanyViewModel
import java.text.SimpleDateFormat
import java.util.*

// Professional color scheme - Balanced for eye comfort
private val PrimaryTeal = Color(0xFF1A4D56)
private val AccentGold = Color(0xFFD4AF37)
private val LightGold = Color(0xFFF0E5C9)
private val DarkTeal = Color(0xFF0F2F35)
private val BackgroundDark = Color(0xFF0A2026)
private val CardBackground = Color(0xFF1E3A41)
private val SecondaryTeal = Color(0xFF2A5F69)

@Composable
fun UpdateCompanyView(
    modifier: Modifier = Modifier,
    updateCompanyViewModel: UpdateCompanyViewModel,
    context: Context,
    onBackClick: () -> Unit = {},
    onUpdateSuccess: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val getCompanyProfileStatus = updateCompanyViewModel.getCompanyProfileStatus.collectAsState().value
    val updateCompanyStatus = updateCompanyViewModel.updateCompanyStatus.collectAsState().value

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var visionMission by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var foundingDate by remember { mutableStateOf("") }

    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        updateCompanyViewModel.getCompanyProfile()
    }

    LaunchedEffect(getCompanyProfileStatus) {
        when (getCompanyProfileStatus) {
            is CompanyStatusUIState.Success -> {
                if (!isLoaded) {
                    val company = getCompanyProfileStatus.companyModel
                    name = company.name
                    address = company.address
                    phoneNumber = company.phone_number
                    website = company.website
                    visionMission = company.vision_mission
                    description = company.description
                    foundingDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(company.founding_date)
                    isLoaded = true
                }
            }
            is CompanyStatusUIState.Failed -> {
                Toast.makeText(
                    context,
                    getCompanyProfileStatus.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                updateCompanyViewModel.clearGetCompanyProfileErrorMessage()
            }
            else -> {}
        }
    }

    LaunchedEffect(updateCompanyStatus) {
        when (updateCompanyStatus) {
            is StringDataStatusUIState.Success -> {
                Toast.makeText(
                    context,
                    "Company profile updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
                updateCompanyViewModel.clearUpdateCompanyStatusErrorMessage()
                onUpdateSuccess()
            }
            is StringDataStatusUIState.Failed -> {
                Toast.makeText(
                    context,
                    updateCompanyStatus.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                updateCompanyViewModel.clearUpdateCompanyStatusErrorMessage()
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
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Update Company Profile",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            when (getCompanyProfileStatus) {
                is CompanyStatusUIState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AccentGold)
                    }
                }
                is CompanyStatusUIState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Company Name *", color = LightGold) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = AccentGold,
                                focusedLabelColor = AccentGold,
                                unfocusedLabelColor = LightGold,
                                focusedContainerColor = CardBackground,
                                unfocusedContainerColor = CardBackground
                            )
                        )

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Address", color = LightGold) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = AccentGold,
                                focusedLabelColor = AccentGold,
                                unfocusedLabelColor = LightGold,
                                focusedContainerColor = CardBackground,
                                unfocusedContainerColor = CardBackground
                            )
                        )

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone Number", color = LightGold) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = AccentGold,
                                focusedLabelColor = AccentGold,
                                unfocusedLabelColor = LightGold,
                                focusedContainerColor = CardBackground,
                                unfocusedContainerColor = CardBackground
                            )
                        )

                        OutlinedTextField(
                            value = website,
                            onValueChange = { website = it },
                            label = { Text("Website", color = LightGold) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = AccentGold,
                                focusedLabelColor = AccentGold,
                                unfocusedLabelColor = LightGold,
                                focusedContainerColor = CardBackground,
                                unfocusedContainerColor = CardBackground
                            )
                        )

                        OutlinedTextField(
                            value = foundingDate,
                            onValueChange = { foundingDate = it },
                            label = { Text("Founding Date (yyyy-MM-dd)", color = LightGold) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("e.g., 2020-01-15", color = Color.White.copy(alpha = 0.5f)) },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = AccentGold,
                                focusedLabelColor = AccentGold,
                                unfocusedLabelColor = LightGold,
                                focusedContainerColor = CardBackground,
                                unfocusedContainerColor = CardBackground
                            )
                        )

                        OutlinedTextField(
                            value = visionMission,
                            onValueChange = { visionMission = it },
                            label = { Text("Vision & Mission", color = LightGold) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 6,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = AccentGold,
                                focusedLabelColor = AccentGold,
                                unfocusedLabelColor = LightGold,
                                focusedContainerColor = CardBackground,
                                unfocusedContainerColor = CardBackground
                            )
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description", color = LightGold) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 6,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = AccentGold,
                                focusedLabelColor = AccentGold,
                                unfocusedLabelColor = LightGold,
                                focusedContainerColor = CardBackground,
                                unfocusedContainerColor = CardBackground
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                if (name.isBlank()) {
                                    Toast.makeText(
                                        context,
                                        "Company name is required",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }

                                val parsedDate = try {
                                    if (foundingDate.isNotBlank()) {
                                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(foundingDate)
                                    } else {
                                        null
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Invalid date format. Please use yyyy-MM-dd",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }

                                updateCompanyViewModel.updateCompanyProfile(
                                    name = name,
                                    address = address.takeIf { it.isNotBlank() },
                                    phoneNumber = phoneNumber.takeIf { it.isNotBlank() },
                                    website = website.takeIf { it.isNotBlank() },
                                    visionMission = visionMission.takeIf { it.isNotBlank() },
                                    description = description.takeIf { it.isNotBlank() },
                                    foundingDate = parsedDate,
                                    logoPath = null,
                                    imagePath = null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentGold,
                                contentColor = PrimaryTeal,
                                disabledContainerColor = AccentGold.copy(alpha = 0.6f)
                            ),
                            enabled = updateCompanyStatus !is StringDataStatusUIState.Loading,
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            if (updateCompanyStatus is StringDataStatusUIState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = PrimaryTeal,
                                    strokeWidth = 3.dp
                                )
                            } else {
                                Text(
                                    text = "Update Profile",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircleLoadingTemplate(
                                color = AccentGold,
                                trackColor = Color.Transparent
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading company profile...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}