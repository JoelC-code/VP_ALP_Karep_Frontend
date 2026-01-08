package com.example.vp_alp_karep_frontend.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vp_alp_karep_frontend.KarepApplication
import com.example.vp_alp_karep_frontend.viewModels.ExperienceViewModel
import com.example.vp_alp_karep_frontend.viewModels.HomeViewModel
import com.example.vp_alp_karep_frontend.viewModels.LoginViewModel
import com.example.vp_alp_karep_frontend.viewModels.RegisterViewModel
import com.example.vp_alp_karep_frontend.viewModels.UserProfileViewModel
import com.example.vp_alp_karep_frontend.viewModels.ViewModelFactory
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.ApplicationViewModel
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.CompanyTagViewModel
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.CompanyViewModel
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.CreateUpdateJobViewModel
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.JobViewModel
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.NotificationViewModel
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModels.UpdateCompanyViewModel
import com.example.vp_alp_karep_frontend.views.CompanyMainScreen
import com.example.vp_alp_karep_frontend.views.CompanyViews.ApplicationView
import com.example.vp_alp_karep_frontend.views.CompanyViews.CompanyProfileView
import com.example.vp_alp_karep_frontend.views.CompanyViews.CompanyTagView
import com.example.vp_alp_karep_frontend.views.CompanyViews.CreateUpdateJobView
import com.example.vp_alp_karep_frontend.views.CompanyViews.JobView
import com.example.vp_alp_karep_frontend.views.CompanyViews.NotificationView
import com.example.vp_alp_karep_frontend.views.CompanyViews.UpdateCompanyView
import com.example.vp_alp_karep_frontend.views.JobDetailScreen
import com.example.vp_alp_karep_frontend.views.LoginView
import com.example.vp_alp_karep_frontend.views.MainScreen
import com.example.vp_alp_karep_frontend.views.RegisterView

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")

    // Company Routes
    object CompanyProfile : Screen("company_profile")
    object UpdateCompanyProfile : Screen("update_company_profile")
    object JobPosts : Screen("job_posts")
    object CreateUpdateJob : Screen("create_update_job") {
        fun createRoute(mode: String = "create", jobId: Int? = null): String {
            return if (mode == "update" && jobId != null) {
                "create_update_job?mode=$mode&jobId=$jobId"
            } else {
                "create_update_job?mode=create"
            }
        }
    }
    object ApplicationsManagement : Screen("applications_management")
    object TagsManagement : Screen("tags_management")
    object Notifications : Screen("notifications")
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
            val uiState = loginViewModel.uiState.collectAsState()

            // Navigate when login is successful
            LaunchedEffect(uiState.value.isLoginSuccessful) {
                if (uiState.value.isLoginSuccessful) {
                    val accountType = uiState.value.accountType
                    if (accountType == "company") {
                        navController.navigate(Screen.Main.route + "?isCompany=true") {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            }

            LoginView(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    // Navigation is handled by LaunchedEffect above
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

        composable(route = Screen.Main.route + "?tab={tab}&isCompany={isCompany}",
            arguments = listOf(
                navArgument("tab") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("isCompany") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val tab = backStackEntry.arguments?.getInt("tab") ?: 0
            val isCompany = backStackEntry.arguments?.getBoolean("isCompany") ?: false

            val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
            val profileViewModel: UserProfileViewModel = viewModel(factory = viewModelFactory)
            val achievementViewModel: com.example.vp_alp_karep_frontend.viewModels.AchievementViewModel = viewModel(factory = viewModelFactory)
            val experienceViewModel: ExperienceViewModel = viewModel(factory = viewModelFactory)

            if (isCompany) {
                CompanyMainScreen(
                    navController = navController,
                    startTab = tab,
                    profileViewModel = profileViewModel,
                    achievementViewModel = achievementViewModel,
                    experienceViewModel = experienceViewModel,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            } else {
                MainScreen(
                    navController = navController,
                    startTab = tab,
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

        composable(
            "job_detail/{jobId}", arguments = listOf(
                navArgument("jobId") { type = NavType.IntType }
            )) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getInt("jobId")
            if (jobId == null) {
                navController.popBackStack()
                return@composable
            }
            JobDetailScreen(jobId, navController)
        }

        // Company Routes
        composable(route = Screen.CompanyProfile.route) {
            val companyViewModel: CompanyViewModel = viewModel(factory = CompanyViewModel.Factory)
            CompanyProfileView(
                navController = navController,
                companyViewModel = companyViewModel,
                context = context
            )
        }

        composable(route = Screen.UpdateCompanyProfile.route) {
            val updateCompanyViewModel: UpdateCompanyViewModel = viewModel(factory = UpdateCompanyViewModel.Factory)
            UpdateCompanyView(
                updateCompanyViewModel = updateCompanyViewModel,
                context = context,
                onBackClick = {
                    navController.popBackStack()
                },
                onUpdateSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.JobPosts.route) {
            val jobViewModel: JobViewModel = viewModel(factory = JobViewModel.Factory)
            JobView(
                jobViewModel = jobViewModel,
                context = context,
                navController = navController,
            )
        }

        composable(
            route = Screen.CreateUpdateJob.route + "?mode={mode}&jobId={jobId}",
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = "create"
                },
                navArgument("jobId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "create"
            val jobId = backStackEntry.arguments?.getInt("jobId")?.takeIf { it != -1 }

            val createUpdateJobViewModel: CreateUpdateJobViewModel = viewModel(
                factory = CreateUpdateJobViewModel.Factory
            )

            // Set mode and jobId when the view is first composed
            LaunchedEffect(mode, jobId) {
                createUpdateJobViewModel.setMode(mode, jobId)
            }

            CreateUpdateJobView(
                createUpdateJobViewModel = createUpdateJobViewModel,
                context = context,
                onBackClick = {
                    navController.popBackStack()
                },
                onSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.ApplicationsManagement.route) {
            val applicationViewModel: ApplicationViewModel = viewModel(factory = ApplicationViewModel.Factory)
            ApplicationView(
                applicationViewModel = applicationViewModel,
                context = context,
            )
        }

        composable(route = Screen.TagsManagement.route) {
            val companyTagViewModel: CompanyTagViewModel = viewModel(factory = CompanyTagViewModel.Factory)
            CompanyTagView(
                companyTagViewModel = companyTagViewModel,
                context = context,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Notifications.route) {
            val notificationViewModel: NotificationViewModel = viewModel(factory = NotificationViewModel.Factory)
            NotificationView(
                notificationViewModel = notificationViewModel,
                context = context,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
