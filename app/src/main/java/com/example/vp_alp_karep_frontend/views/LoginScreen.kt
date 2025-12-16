//TODO HAPUS JIKA SUDAH NYAMBUNG KE FRONTEND
package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.example.vp_alp_karep_frontend.uiStates.LoginUiStates
import com.example.vp_alp_karep_frontend.viewModels.FakeLoginViewModel

@Composable
fun LoginScreen(
    viewModel: FakeLoginViewModel,
    navController: NavHostController
) {
    when(val state = viewModel.authStatus){
        LoginUiStates.Start -> {
            Column {
                Button(onClick = viewModel::loginUser) {
                    Text("User")
                }
                Button(onClick = viewModel::loginCompany) {
                    Text("Company")
                }
            }
        }

        LoginUiStates.Loading -> {
            CircularProgressIndicator()
        }

        is LoginUiStates.Success -> {
            val user = state.result.user
            val token = state.result.token

            val canApply = viewModel.canApply

            if(user == null) {
                Text("Login Succeded, no user detected")
                return
            }

            LaunchedEffect(user.id) {
                navController.navigate(
                    "dashboard/${user.name}/${user.email}?token=$token&canApply=$canApply"
                ) {
                    popUpTo("login") { inclusive = true }
                }
                viewModel.cachedToken = token
                viewModel.resetState()
            }
        }

        is LoginUiStates.Error -> {
            Text(state.message)
        }
    }
}