package com.example.vp_alp_karep_frontend.routes

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.vp_alp_karep_frontend.enums.JobListStatus
import com.example.vp_alp_karep_frontend.views.ApplicationScreen
import com.example.vp_alp_karep_frontend.views.JobDetailScreen
import com.example.vp_alp_karep_frontend.views.JobListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopAppBar(
    title: String,
    navController: NavHostController
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

    fun resolveTopBarTitle(route: String?): String? =
        when {
            route == "job-tags" -> null
            route == "jobs" -> "All Jobs"
            route == "jobs-company" -> "Company Jobs"
            route?.startsWith("job_detail") == true -> "Job Detail"
            route == "my-application" -> "My Applications"
            else -> null
        }


    @Composable
    fun MainNavGraph(
        navController: NavHostController,
        token: String,
        canApply: Boolean
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val route = backStackEntry?.destination?.route
        val title = resolveTopBarTitle(route)
        Scaffold(
            topBar = {
                title?.let {
                    BackTopAppBar(
                        title = it,
                        navController = navController
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "jobs",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("jobs") {
                    JobListScreen(token = token, navController, JobListStatus.ALL)
                }

                composable("jobs-company") {
                    JobListScreen(token = token, navController, JobListStatus.COMPANY)
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
                    JobDetailScreen(token = token, jobId, canApply, navController)
                }

                composable("my-application") {
                    ApplicationScreen(token = token)
                }
            }
        }
    }
