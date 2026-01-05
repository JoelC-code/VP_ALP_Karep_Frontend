//HAPUS JIKA SUDAH SELESAI
package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.vp_alp_karep_frontend.routes.MainNavGraph

@Composable
fun DashBoardScreen(
    name: String,
    email: String,
    token: String,
    canApply: Boolean,
) {
    val mainNavController = rememberNavController()
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = "Welcome, $name",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Email: $email")

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { mainNavController.navigate("jobs") }
        ) {
            Text("All Jobs")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { mainNavController.navigate("jobs-company") }
        ) {
            Text("Company Jobs")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { mainNavController.navigate("my-application") }
        ) {
            Text("My Applications")
        }

        MainNavGraph(
            navController = mainNavController,
            token = token,
            canApply = canApply
        )
    }
}