package com.example.vp_alp_karep_frontend.views.CompanyViews

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vp_alp_karep_frontend.R
import com.example.vp_alp_karep_frontend.navigation.Screen
import com.example.vp_alp_karep_frontend.uiStates.CompanyUIStates.CompanyStatusUIState
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.CompanyViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Professional color scheme - Balanced for eye comfort
private val PrimaryTeal = Color(0xFF1A4D56)
private val AccentGold = Color(0xFFD4AF37)
private val LightGold = Color(0xFFF0E5C9)
private val DarkTeal = Color(0xFF0F2F35)
private val BackgroundDark = Color(0xFF0A2026)
private val CardBackground = Color(0xFF1E3A41)
private val SecondaryTeal = Color(0xFF2A5F69)

fun getTagColor(index: Int): Color {
    val colors = listOf(
        AccentGold,
        PrimaryTeal,
        SecondaryTeal,
        DarkTeal,
        Color(0xFF8B7355)
    )
    return colors[index % colors.size]
}

@Composable
fun CompanyProfileView(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context,
    companyViewModel: CompanyViewModel
) {
    val scrollState = rememberScrollState()
    val getCompanyProfileStatus = companyViewModel.getCompanyProfileStatus.collectAsState().value

    LaunchedEffect(Unit) {
        companyViewModel.getCompanyProfile()
    }

    LaunchedEffect(getCompanyProfileStatus) {
        if (getCompanyProfileStatus is CompanyStatusUIState.Failed) {
            Toast.makeText(
                context,
                getCompanyProfileStatus.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
            companyViewModel.clearGetCompanyProfileErrorMessage()
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
        when (getCompanyProfileStatus) {
            is CompanyStatusUIState.Success -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Company Profile Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBackground
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Company Logo",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = getCompanyProfileStatus.companyModel.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = getCompanyProfileStatus.companyModel.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = LightGold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = getCompanyProfileStatus.companyModel.phone_number,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = getCompanyProfileStatus.companyModel.website,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AccentGold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${getCompanyProfileStatus.companyModel.total_jobs}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGold
                                )
                                Text(
                                    text = "Total Jobs",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = formatDate(getCompanyProfileStatus.companyModel.founding_date),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGold
                                )
                                Text(
                                    text = "Founded",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Company Image Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Company Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Company Details Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBackground
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "Address",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AccentGold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = getCompanyProfileStatus.companyModel.address,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AccentGold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = getCompanyProfileStatus.companyModel.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Vision Mission",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AccentGold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = getCompanyProfileStatus.companyModel.vision_mission,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Company Tags Section
                if (getCompanyProfileStatus.companyModel.company_tags.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CardBackground
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Text(
                                text = "Company Tags",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AccentGold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                mainAxisSpacing = 10.dp,
                                crossAxisSpacing = 12.dp
                            ) {
                                getCompanyProfileStatus.companyModel.company_tags.forEachIndexed { index, tag ->
                                    Box(
                                        modifier = Modifier
                                            .shadow(
                                                elevation = 3.dp,
                                                shape = RoundedCornerShape(24.dp),
                                                clip = false
                                            )
                                            .background(
                                                color = getTagColor(index),
                                                shape = RoundedCornerShape(24.dp)
                                            )
                                            .padding(horizontal = 20.dp, vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = "# ${tag.name}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Quick Actions Section
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = {
                        navController.navigate(Screen.UpdateCompanyProfile.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentGold,
                        contentColor = PrimaryTeal
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        navController.navigate(Screen.TagsManagement.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CardBackground,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Manage Tags",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            is CompanyStatusUIState.Loading -> {
                Column(
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

            else -> {}
        }
    }
}

private fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy", Locale.getDefault())
    return formatter.format(date)
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    mainAxisSpacing: Dp = 0.dp,
    crossAxisSpacing: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val mainAxisSpacingPx = mainAxisSpacing.roundToPx()
        val crossAxisSpacingPx = crossAxisSpacing.roundToPx()

        val rows = mutableListOf<MutableList<Placeable>>()
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0

        measurables.forEach { measurable ->
            val placeable = measurable.measure(constraints)

            if (currentRowWidth + placeable.width > constraints.maxWidth && currentRow.isNotEmpty()) {
                rows.add(currentRow)
                currentRow = mutableListOf()
                currentRowWidth = 0
            }

            currentRow.add(placeable)
            currentRowWidth += placeable.width + mainAxisSpacingPx
        }

        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }

        val width = constraints.maxWidth
        val height = rows.mapIndexed { index, row ->
            val maxHeight = row.maxOfOrNull { it.height } ?: 0
            maxHeight + if (index < rows.size - 1) crossAxisSpacingPx else 0
        }.sum()

        layout(width, height) {
            var yPosition = 0

            rows.forEach { row ->
                var xPosition = 0
                val maxRowHeight = row.maxOfOrNull { it.height } ?: 0

                row.forEach { placeable ->
                    placeable.placeRelative(x = xPosition, y = yPosition)
                    xPosition += placeable.width + mainAxisSpacingPx
                }

                yPosition += maxRowHeight + crossAxisSpacingPx
            }
        }
    }
}
