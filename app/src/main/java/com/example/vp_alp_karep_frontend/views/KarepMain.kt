package com.example.vp_alp_karep_frontend.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vp_alp_karep_frontend.enums.PageEnums
import com.example.vp_alp_karep_frontend.viewModels.ApplicationViewModel
import com.example.vp_alp_karep_frontend.viewModels.CompanyTagViewModel
import com.example.vp_alp_karep_frontend.viewModels.CompanyViewModel
import com.example.vp_alp_karep_frontend.viewModels.CreateUpdateJobViewModel
import com.example.vp_alp_karep_frontend.viewModels.JobViewModel
import com.example.vp_alp_karep_frontend.viewModels.NotificationViewModel
import com.example.vp_alp_karep_frontend.viewModels.UpdateCompanyViewModel

@Composable
fun KarepMain(
    navController: NavHostController = rememberNavController(),
    companyViewModel: CompanyViewModel = viewModel(factory = CompanyViewModel.Factory),
    notificationViewModel: NotificationViewModel = viewModel(factory = NotificationViewModel.Factory),
    applicationViewModel: ApplicationViewModel = viewModel(factory = ApplicationViewModel.Factory),
    updateCompanyViewModel: UpdateCompanyViewModel = viewModel(factory = UpdateCompanyViewModel.Factory),
    jobViewModel: JobViewModel = viewModel(factory = JobViewModel.Factory),
    createUpdateJobViewModel: CreateUpdateJobViewModel = viewModel(factory = CreateUpdateJobViewModel.Factory),
    companyTagViewModel: CompanyTagViewModel = viewModel(factory = CompanyTagViewModel.Factory)
) {
    val localContext = LocalContext.current
    val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwibmFtZSI6InVzZXIiLCJlbWFpbCI6InVzZXJAZW1haWwuY29tIiwiaWF0IjoxNzY3Njg0Njc3LCJleHAiOjE3NzAyNzY2Nzd9.R7xrgud_RXyHFeLYnseLOKRHl1ATt2iiAIzedcRAm8Y"

    NavHost(
        navController = navController,
        startDestination = PageEnums.CompanyProfile.name
    ) {
        composable(route = PageEnums.CompanyProfile.name) {
            CompanyProfileView(
                navController = navController,
                companyViewModel = companyViewModel,
                token = token,
                context = localContext
            )
        }
        composable(route = PageEnums.UpdateCompanyProfile.name) {
            UpdateCompanyView(
                updateCompanyViewModel = updateCompanyViewModel,
                token = token,
                context = localContext,
                onBackClick = {
                    navController.popBackStack()
                },
                onUpdateSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = PageEnums.JobPosts.name) {
            JobCompanyView(
                jobViewModel = jobViewModel,
                createUpdateJobViewModel = createUpdateJobViewModel,
                token = token,
                context = localContext,
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToCreateUpdate = {
                    navController.navigate(PageEnums.CreateUpdateJob.name)
                }
            )
        }
        composable(route = PageEnums.CreateUpdateJob.name) {
            CreateUpdateJobView(
                createUpdateJobViewModel = createUpdateJobViewModel,
                token = token,
                context = localContext,
                onBackClick = {
                    navController.popBackStack()
                },
                onSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = PageEnums.ApplicationsManagement.name) {
            ApplicationManagementCompanyView(
                applicationViewModel = applicationViewModel,
                token = token,
                context = localContext,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = PageEnums.TagsManagement.name) {
            CompanyTagView(
                companyTagViewModel = companyTagViewModel,
                token = token,
                context = localContext,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = PageEnums.Notifications.name) {
            NotificationView(
                notificationViewModel = notificationViewModel,
                token = token,
                context = localContext,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}