package com.example.vp_alp_karep_frontend.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.viewModels.ExperienceViewModel
import com.example.vp_alp_karep_frontend.viewModels.HomeViewModel
import com.example.vp_alp_karep_frontend.viewModels.LoginViewModel
import com.example.vp_alp_karep_frontend.viewModels.RegisterViewModel
import com.example.vp_alp_karep_frontend.viewModels.UserProfileViewModel
import com.example.vp_alp_karep_frontend.viewModels.ViewModelFactory
import com.example.vp_alp_karep_frontend.views.LoginView
import com.example.vp_alp_karep_frontend.views.MainScreen
import com.example.vp_alp_karep_frontend.views.RegisterView

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val application = context.applicationContext as? KarepApplication
        ?: throw IllegalStateException("Application must be KarepApplication")
    val container = application.container

    val viewModelFactory = ViewModelFactory(
        loginRegistRepository = container.loginRegistRepository,
        userProfileRepository = container.userProfileRepository,
        userExperienceRepository = container.userExperienceRepository,
        userAchievementRepository = container.userAchievementRepository
    )

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(factory = viewModelFactory)
            LoginView(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel(factory = viewModelFactory)
            RegisterView(
                viewModel = registerViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Main.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
            val profileViewModel: UserProfileViewModel = viewModel(factory = viewModelFactory)
            val achievementViewModel: com.example.vp_alp_karep_frontend.viewModels.AchievementViewModel = viewModel(factory = viewModelFactory)
            val experienceViewModel: ExperienceViewModel = viewModel(factory = viewModelFactory)
            MainScreen(
                homeViewModel = homeViewModel,
                profileViewModel = profileViewModel,
                achievementViewModel = achievementViewModel,
                experienceViewModel = experienceViewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
