package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.vp_alp_karep_frontend.viewModels.HomeViewModel
import com.example.vp_alp_karep_frontend.viewModels.UserProfileViewModel

// Professional color scheme - Balanced for eye comfort
private val PrimaryTeal = Color(0xFF1A4D56)      // Balanced teal - not too bright, not too dark
private val AccentGold = Color(0xFFD4AF37)       // Brighter gold for better contrast
private val DarkTeal = Color(0xFF0F2F35)         // Soft dark teal for depth
private val CardBackground = Color(0xFF1E3A41)   // Comfortable card background
private val BackgroundGradientStart = Color(0xFF0A2026)  // Subtle dark for gradient
private val BackgroundGradientEnd = Color(0xFF1A4D56)    // Matches primary teal

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Beranda")
    object Saved : BottomNavItem("saved", Icons.Default.FavoriteBorder, "Aktivitas")
    object Career : BottomNavItem("career", Icons.Default.Star, "Karier")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profil")
}

@Composable
fun MainScreen(
    homeViewModel: HomeViewModel,
    profileViewModel: UserProfileViewModel,
    achievementViewModel: com.example.vp_alp_karep_frontend.viewModels.AchievementViewModel,
    experienceViewModel: com.example.vp_alp_karep_frontend.viewModels.ExperienceViewModel,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Saved,
        BottomNavItem.Career,
        BottomNavItem.Profile
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
                0 -> HomeView(
                    viewModel = homeViewModel,
                    userName = "Jason"
                )
                1 -> SavedJobsView()
                2 -> CareerView()
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

@Composable
fun SavedJobsView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = AccentGold.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Belum ada pekerjaan yang disimpan",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CareerView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = AccentGold.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Karier - Segera Hadir",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}

