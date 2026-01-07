package com.example.vp_alp_karep_frontend.views

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vp_alp_karep_frontend.viewModels.UserProfileViewModel
import com.example.vp_alp_karep_frontend.viewModels.AchievementViewModel
import com.example.vp_alp_karep_frontend.viewModels.ExperienceViewModel

// ============================================================================
// PROFESSIONAL COLOR PALETTE - Inspired by LinkedIn & Modern Job Apps
// ============================================================================
private val PrimaryBlue = Color(0xFF0A66C2)           // LinkedIn Blue - Trust & Professional
private val SecondaryBlue = Color(0xFF378FE9)         // Lighter Blue for accents
private val AccentGold = Color(0xFFFFB800)            // Warm accent for CTAs
private val BackgroundLight = Color(0xFFF3F6F8)       // Soft background
private val BackgroundDark = Color(0xFF0D1B2A)        // Rich dark blue
private val CardLight = Color(0xFFFFFFFF)             // Pure white cards
private val CardDark = Color(0xFF1B263B)              // Dark mode cards
private val TextPrimary = Color(0xFF000000)           // Black text
private val TextSecondary = Color(0xFF666666)         // Gray text
private val TextTertiary = Color(0xFF999999)          // Light gray
private val SuccessGreen = Color(0xFF057A55)          // Success states
private val ErrorRed = Color(0xFFDC2626)              // Error states
private val BorderColor = Color(0xFFE5E7EB)           // Subtle borders
private val DividerColor = Color(0xFFF0F0F0)          // Dividers
private val GradientStart = Color(0xFF0A66C2)         // Gradient start
private val GradientEnd = Color(0xFF378FE9)           // Gradient end

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileViewNew(
    viewModel: UserProfileViewModel,
    achievementViewModel: AchievementViewModel? = null,
    experienceViewModel: ExperienceViewModel? = null,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(0) }
    var isEditMode by remember { mutableStateOf(false) }
    val tabs = listOf(
        TabItem("Personal", Icons.Outlined.Person),
        TabItem("Account", Icons.Outlined.Lock),
        TabItem("Achievements", Icons.Outlined.Star),
        TabItem("Experience", Icons.Outlined.Business)
    )

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        achievementViewModel?.loadAchievements()
        experienceViewModel?.loadExperiences()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // ====================================================================
            // PROFESSIONAL HEADER WITH GRADIENT & GLASSMORPHISM
            // ====================================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                // Gradient Background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(GradientStart, GradientEnd),
                                startY = 0f,
                                endY = 800f
                            )
                        )
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Bar with Icons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { /* Notifications */ },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }

                        AnimatedContent(
                            targetState = isEditMode,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith
                                        fadeOut(animationSpec = tween(300))
                            }
                        ) { editMode ->
                            IconButton(
                                onClick = { isEditMode = !isEditMode },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        if (editMode) AccentGold else Color.White.copy(alpha = 0.2f),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = if (editMode) Icons.Filled.Check else Icons.Outlined.Edit,
                                    contentDescription = if (editMode) "Save" else "Edit",
                                    tint = if (editMode) Color.White else Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Profile Avatar with Border
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(8.dp, CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(AccentGold, Color(0xFFFFD700))
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // User Name with Animation
                    Text(
                        text = uiState.user?.fullName ?: uiState.user?.username ?: "User",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Location Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = uiState.user?.address ?: "Add location",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ====================================================================
            // MODERN TAB ROW WITH ICONS
            // ====================================================================
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = PrimaryBlue,
                edgePadding = 0.dp,
                divider = {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = DividerColor
                    )
                },
                modifier = Modifier.shadow(2.dp)
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        modifier = Modifier.height(64.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = if (selectedTab == index) PrimaryBlue else TextTertiary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                color = if (selectedTab == index) PrimaryBlue else TextTertiary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ====================================================================
            // TAB CONTENT WITH ANIMATIONS
            // ====================================================================
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                }
            ) { tab ->
                when (tab) {
                    0 -> ModernPersonalDataSection(uiState, viewModel, isEditMode)
                    1 -> ModernAccountDataSection(viewModel, isEditMode)
                    2 -> achievementViewModel?.let {
                        ModernAchievementSection(it, isEditMode)
                    }
                    3 -> experienceViewModel?.let {
                        ModernExperienceSection(it, isEditMode)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ====================================================================
            // PROFESSIONAL LOGOUT BUTTON
            // ====================================================================
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ExitToApp,
                        contentDescription = null,
                        tint = ErrorRed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Sign Out",
                        color = ErrorRed,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// ============================================================================
// DATA CLASS FOR TABS
// ============================================================================
data class TabItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

// ============================================================================
// MODERN PERSONAL DATA SECTION
// ============================================================================
@Composable
fun ModernPersonalDataSection(
    uiState: com.example.vp_alp_karep_frontend.uiStates.ProfileUiState,
    viewModel: UserProfileViewModel,
    isEditMode: Boolean
) {
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Personal Information",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                Text(
                    text = "Manage your personal details",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            AnimatedVisibility(visible = isEditMode) {
                IconButton(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(PrimaryBlue.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Modern Card with Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardLight
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ModernInfoRow(
                    icon = Icons.Outlined.Person,
                    label = "Full Name",
                    value = uiState.user?.fullName ?: "Not set"
                )
                ModernInfoRow(
                    icon = Icons.Outlined.Email,
                    label = "Email",
                    value = uiState.user?.email ?: "Not set"
                )
                ModernInfoRow(
                    icon = Icons.Outlined.Phone,
                    label = "Phone",
                    value = uiState.user?.phoneNumber ?: "Not set"
                )
                ModernInfoRow(
                    icon = Icons.Outlined.LocationOn,
                    label = "Address",
                    value = uiState.user?.address ?: "Not set"
                )
            }
        }
    }

    if (showEditDialog) {
        EditProfileDialogNew(
            currentFullName = uiState.user?.fullName ?: "",
            currentPhoneNumber = uiState.user?.phoneNumber ?: "",
            currentAddress = uiState.user?.address ?: "",
            isLoading = uiState.isLoading,
            onDismiss = { showEditDialog = false },
            onSave = { fullName: String?, phoneNumber: String?, address: String? ->
                viewModel.updateProfile(fullName, phoneNumber, address)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun ModernInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(PrimaryBlue.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = TextPrimary,
                fontSize = 15.sp
            )
        }
    }
}

// ============================================================================
// MODERN ACCOUNT DATA SECTION
// ============================================================================
@Composable
fun ModernAccountDataSection(viewModel: UserProfileViewModel, isEditMode: Boolean) {
    var showUpdateEmailDialog by remember { mutableStateOf(false) }
    var showUpdatePasswordDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Column {
            Text(
                text = "Account Security",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
            Text(
                text = "Manage your login credentials",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        // Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardLight
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Email Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(SuccessGreen.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Email Address",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = uiState.user?.email ?: "Not set",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = TextPrimary
                            )
                        }
                    }
                    AnimatedVisibility(visible = isEditMode) {
                        TextButton(onClick = { showUpdateEmailDialog = true }) {
                            Text("Change", color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                HorizontalDivider(color = DividerColor)

                // Password Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(AccentGold.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Password",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "••••••••",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = TextPrimary
                            )
                        }
                    }
                    AnimatedVisibility(visible = isEditMode) {
                        TextButton(onClick = { showUpdatePasswordDialog = true }) {
                            Text("Change", color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Security Tips Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = PrimaryBlue.copy(alpha = 0.05f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Keep your account secure by using a strong password and updating it regularly.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }
    }

    if (showUpdateEmailDialog) {
        UpdateEmailDialogNew(
            isLoading = uiState.isLoading,
            errorMessage = uiState.errorMessage,
            onDismiss = { showUpdateEmailDialog = false },
            onUpdate = { currentPassword: String, newEmail: String ->
                viewModel.updateEmail(currentPassword, newEmail)
            }
        )
    }

    if (showUpdatePasswordDialog) {
        UpdatePasswordDialogNew(
            isLoading = uiState.isLoading,
            errorMessage = uiState.errorMessage,
            onDismiss = { showUpdatePasswordDialog = false },
            onUpdate = { currentPassword: String, newPassword: String ->
                viewModel.updatePassword(currentPassword, newPassword)
            }
        )
    }
}

// ============================================================================
// MODERN ACHIEVEMENT SECTION
// ============================================================================
@Composable
fun ModernAchievementSection(viewModel: AchievementViewModel, isEditMode: Boolean) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Achievements",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                Text(
                    text = "Showcase your accomplishments",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            AnimatedVisibility(visible = isEditMode) {
                FilledIconButton(
                    onClick = { /* Add achievement */ },
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = AccentGold
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }

        when {
            uiState.isLoading -> {
                repeat(3) {
                    ShimmerAchievementCard()
                }
            }
            uiState.achievements.isEmpty() -> {
                EmptyStateCard(
                    icon = Icons.Outlined.Star,
                    title = "No achievements yet",
                    description = "Add your achievements to showcase your success"
                )
            }
            else -> {
                uiState.achievements.forEachIndexed { index, achievement ->
                    ModernAchievementCard(
                        title = achievement.title,
                        description = achievement.description ?: "",
                        index = index
                    )
                }
            }
        }
    }
}

@Composable
fun ModernAchievementCard(title: String, description: String, index: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardLight
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Achievement Icon with gradient
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(AccentGold, Color(0xFFFFD700))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

// ============================================================================
// MODERN EXPERIENCE SECTION
// ============================================================================
@Composable
fun ModernExperienceSection(viewModel: ExperienceViewModel, isEditMode: Boolean) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Work Experience",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                Text(
                    text = "Your professional journey",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            AnimatedVisibility(visible = isEditMode) {
                FilledIconButton(
                    onClick = { /* Add experience */ },
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = PrimaryBlue
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }

        when {
            uiState.isLoading -> {
                repeat(3) {
                    ShimmerExperienceCard()
                }
            }
            uiState.experiences.isEmpty() -> {
                EmptyStateCard(
                    icon = Icons.Default.Business,
                    title = "No experience added",
                    description = "Add your work experience to enhance your profile"
                )
            }
            else -> {
                uiState.experiences.forEachIndexed { index, experience ->
                    ModernExperienceCard(
                        title = experience.title,
                        description = experience.description ?: "",
                        isLast = index == uiState.experiences.size - 1
                    )
                }
            }
        }
    }
}

@Composable
fun ModernExperienceCard(title: String, description: String, isLast: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardLight
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Company Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        PrimaryBlue.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Folder,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = TextTertiary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ============================================================================
// EMPTY STATE COMPONENT
// ============================================================================
@Composable
fun EmptyStateCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardLight
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        TextTertiary.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextTertiary,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ============================================================================
// SHIMMER LOADING EFFECTS
// ============================================================================
@Composable
fun ShimmerAchievementCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(TextTertiary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(16.dp)
                        .background(TextTertiary.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .background(TextTertiary.copy(alpha = 0.05f), RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
fun ShimmerExperienceCard() {
    ShimmerAchievementCard()
}

// ============================================================================
// MODERN DIALOGS
// ============================================================================
@Composable
fun EditProfileDialogNew(
    currentFullName: String,
    currentPhoneNumber: String,
    currentAddress: String,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (String?, String?, String?) -> Unit
) {
    var fullName by remember { mutableStateOf(currentFullName) }
    var phoneNumber by remember { mutableStateOf(currentPhoneNumber) }
    var address by remember { mutableStateOf(currentAddress) }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = {
            Text(
                "Edit Profile",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Outlined.Person, null, tint = PrimaryBlue)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        focusedLabelColor = PrimaryBlue
                    )
                )
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Outlined.Phone, null, tint = PrimaryBlue)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        focusedLabelColor = PrimaryBlue
                    )
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    minLines = 2,
                    leadingIcon = {
                        Icon(Icons.Outlined.LocationOn, null, tint = PrimaryBlue)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        focusedLabelColor = PrimaryBlue
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(fullName.ifBlank { null }, phoneNumber.ifBlank { null }, address.ifBlank { null }) },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                else Text("Save", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text("Cancel", color = TextSecondary)
            }
        },
        containerColor = CardLight
    )
}

@Composable
fun UpdateEmailDialogNew(
    isLoading: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onUpdate: (String, String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Update Email", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary) },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = PrimaryBlue) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, focusedLabelColor = PrimaryBlue)
                )
                OutlinedTextField(
                    value = newEmail,
                    onValueChange = { newEmail = it },
                    label = { Text("New Email") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    leadingIcon = { Icon(Icons.Outlined.Email, null, tint = PrimaryBlue) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, focusedLabelColor = PrimaryBlue)
                )
                if (errorMessage != null) Text(text = errorMessage, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            Button(
                onClick = { if (currentPassword.isNotBlank() && newEmail.isNotBlank()) onUpdate(currentPassword, newEmail) },
                enabled = !isLoading && currentPassword.isNotBlank() && newEmail.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                else Text("Update", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss, enabled = !isLoading) { Text("Cancel", color = TextSecondary) } },
        containerColor = CardLight
    )
}

@Composable
fun UpdatePasswordDialogNew(
    isLoading: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onUpdate: (String, String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Update Password", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary) },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it; localError = null },
                    label = { Text("Current Password") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = PrimaryBlue) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, focusedLabelColor = PrimaryBlue)
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it; localError = null },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = PrimaryBlue) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, focusedLabelColor = PrimaryBlue)
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; localError = null },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = PrimaryBlue) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, focusedLabelColor = PrimaryBlue)
                )
                val displayError = localError ?: errorMessage
                if (displayError != null) Text(text = displayError, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        currentPassword.isBlank() -> localError = "Current password required"
                        newPassword.isBlank() -> localError = "New password required"
                        newPassword.length < 8 -> localError = "Password must be 8+ characters"
                        newPassword != confirmPassword -> localError = "Passwords don't match"
                        else -> onUpdate(currentPassword, newPassword)
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                else Text("Update", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss, enabled = !isLoading) { Text("Cancel", color = TextSecondary) } },
        containerColor = CardLight
    )
}
