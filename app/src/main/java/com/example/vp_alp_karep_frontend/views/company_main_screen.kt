package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.ApplicationViewModel
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.CompanyViewModel
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.JobViewModel
import com.example.vp_alp_karep_frontend.viewModels.UserProfileViewModel
import com.example.vp_alp_karep_frontend.views.CompanyViews.ApplicationView
import com.example.vp_alp_karep_frontend.views.CompanyViews.CompanyProfileView
import com.example.vp_alp_karep_frontend.views.CompanyViews.JobView

// Professional color scheme - same as user main screen
private val PrimaryTeal = Color(0xFF1A4D56)
private val AccentGold = Color(0xFFD4AF37)
private val DarkTeal = Color(0xFF0F2F35)
private val CardBackground = Color(0xFF1E3A41)
private val BackgroundGradientStart = Color(0xFF0A2026)
private val BackgroundGradientEnd = Color(0xFF1A4D56)

sealed class CompanyBottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object CompanyProfile : CompanyBottomNavItem("company_profile", Icons.Default.Build, "Company")
    object Jobs : CompanyBottomNavItem("jobs", Icons.Default.Work, "Jobs")
    object Applications : CompanyBottomNavItem("applications", Icons.Default.Inbox, "Application")
    object Profile : CompanyBottomNavItem("profile", Icons.Default.Person, "Profil")
}

@Composable
fun CompanyMainScreen(
    navController: NavHostController,
    startTab: Int = 0,
    profileViewModel: UserProfileViewModel,
    achievementViewModel: com.example.vp_alp_karep_frontend.viewModels.AchievementViewModel,
    experienceViewModel: com.example.vp_alp_karep_frontend.viewModels.ExperienceViewModel,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(startTab) }
    val context = LocalContext.current

    val items = listOf(
        CompanyBottomNavItem.CompanyProfile,
        CompanyBottomNavItem.Jobs,
        CompanyBottomNavItem.Applications,
        CompanyBottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = CardBackground,
                contentColor = Color.White,
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentGold,
                            selectedTextColor = AccentGold,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = PrimaryTeal.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> {
                    val companyViewModel: CompanyViewModel = viewModel(factory = CompanyViewModel.Factory)
                    CompanyProfileView(
                        navController = navController,
                        companyViewModel = companyViewModel,
                        context = context
                    )
                }
                1 -> {
                    val jobViewModel: JobViewModel = viewModel(factory = JobViewModel.Factory)
                    JobView(
                        jobViewModel = jobViewModel,
                        context = context,
                        navController = navController
                    )
                }
                2 -> {
                    val applicationViewModel: ApplicationViewModel = viewModel(factory = ApplicationViewModel.Factory)
                    ApplicationView(
                        applicationViewModel = applicationViewModel,
                        context = context
                    )
                }
                3 -> UserProfileViewNew(
                    viewModel = profileViewModel,
                    achievementViewModel = achievementViewModel,
                    experienceViewModel = experienceViewModel,
                    onLogout = onLogout
                )
            }
        }
    }
}

