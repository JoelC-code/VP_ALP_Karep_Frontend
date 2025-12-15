//HAPUS KALAU UDAH JADIIN SATU DENGAN NAVGRAPH UTAMA
package com.example.vp_alp_karep_frontend

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vp_alp_karep_frontend.viewModels.fakeLoginViewModel
import com.example.vp_alp_karep_frontend.views.DashBoardScreen
import com.example.vp_alp_karep_frontend.views.LoginScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: fakeLoginViewModel = viewModel(factory = fakeLoginViewModel.Factory)
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            //dashboard/${user.name}/${user.email}
            route = "dashboard/{name}/{email}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            DashBoardScreen(
                name = backStackEntry.arguments?.getString("name") ?: "Unknown Name",
                email = backStackEntry.arguments?.getString("email") ?: "Unknown Email"
            )
        }
    }
}