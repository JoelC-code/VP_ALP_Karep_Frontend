package com.example.vp_alp_karep_frontend.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vp_alp_karep_frontend.repositories.LoginRegistRepository
import com.example.vp_alp_karep_frontend.repositories.UserExperienceRepository
import com.example.vp_alp_karep_frontend.repositories.UserProfileRepository
import com.example.vp_alp_karep_frontend.repositories.UserAchievementRepository

class ViewModelFactory(
    private val loginRegistRepository: LoginRegistRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userExperienceRepository: UserExperienceRepository,
    private val userAchievementRepository: UserAchievementRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(loginRegistRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(loginRegistRepository) as T
            }
            modelClass.isAssignableFrom(UserProfileViewModel::class.java) -> {
                UserProfileViewModel(userProfileRepository, loginRegistRepository) as T
            }
            modelClass.isAssignableFrom(ExperienceViewModel::class.java) -> {
                ExperienceViewModel(userExperienceRepository, loginRegistRepository) as T
            }
            modelClass.isAssignableFrom(AchievementViewModel::class.java) -> {
                AchievementViewModel(userAchievementRepository, loginRegistRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(loginRegistRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

