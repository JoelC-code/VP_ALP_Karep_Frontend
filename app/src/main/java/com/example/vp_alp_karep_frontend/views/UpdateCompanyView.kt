package com.example.vp_alp_karep_frontend.views

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_alp_karep_frontend.uiStates.CompanyStatusUIState
import com.example.vp_alp_karep_frontend.uiStates.StringDataStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.UpdateCompanyViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun UpdateCompanyView(
    modifier: Modifier = Modifier,
    updateCompanyViewModel: UpdateCompanyViewModel,
    token: String,
    context: Context,
    onBackClick: () -> Unit = {},
    onUpdateSuccess: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val getCompanyProfileStatus = updateCompanyViewModel.getCompanyProfileStatus
    val updateCompanyStatus = updateCompanyViewModel.updateCompanyStatus

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var visionMission by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var foundingDate by remember { mutableStateOf("") }

    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            updateCompanyViewModel.getCompanyProfile(token)
        }
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        // Top Bar with Back Button
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
                text = "Update Company Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        when (getCompanyProfileStatus) {
            is CompanyStatusUIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
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
                    // Company Name Field (Required)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Company Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    // Address Field
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Address") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    // Phone Number Field
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    // Website Field
                    OutlinedTextField(
                        value = website,
                        onValueChange = { website = it },
                        label = { Text("Website") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    // Founding Date Field
                    OutlinedTextField(
                        value = foundingDate,
                        onValueChange = { foundingDate = it },
                        label = { Text("Founding Date (yyyy-MM-dd)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("e.g., 2020-01-15") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    // Vision Mission Field
                    OutlinedTextField(
                        value = visionMission,
                        onValueChange = { visionMission = it },
                        label = { Text("Vision & Mission") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 6,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    // Description Field
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 6,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Update Button
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
                                token = token,
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
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            contentColor = Color.White
                        ),
                        enabled = updateCompanyStatus !is StringDataStatusUIState.Loading
                    ) {
                        if (updateCompanyStatus is StringDataStatusUIState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Update Profile",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
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
                    Text(
                        text = "Loading company profile...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}