package com.example.vp_alp_karep_frontend.views

// This file has been replaced by user_profile_view_new.kt
// Please use UserProfileViewNew instead of UserProfileView
// Main screen uses UserProfileViewNew from user_profile_view_new.kt

import androidx.compose.runtime.Composable
import com.example.vp_alp_karep_frontend.viewModels.AchievementViewModel
import com.example.vp_alp_karep_frontend.viewModels.ExperienceViewModel
import com.example.vp_alp_karep_frontend.viewModels.UserProfileViewModel

/**
 * DEPRECATED - Use UserProfileViewNew instead
 * This function exists only for backward compatibility
 * Main application uses UserProfileViewNew from user_profile_view_new.kt
 */
@Composable
@Deprecated(
    message = "Use UserProfileViewNew from user_profile_view_new.kt instead",
    replaceWith = ReplaceWith("UserProfileViewNew(viewModel, achievementViewModel, experienceViewModel, onLogout)")
)
fun UserProfileView(
    viewModel: UserProfileViewModel,
    achievementViewModel: AchievementViewModel? = null,
    experienceViewModel: ExperienceViewModel? = null,
    onLogout: () -> Unit
) {
    // Redirect to the new implementation
    UserProfileViewNew(
        viewModel = viewModel,
        achievementViewModel = achievementViewModel,
        experienceViewModel = experienceViewModel,
        onLogout = onLogout
    )
}

