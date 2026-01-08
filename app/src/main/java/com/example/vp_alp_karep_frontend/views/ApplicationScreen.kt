package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vp_alp_karep_frontend.viewModels.ApplicationListViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vp_alp_karep_frontend.uiStates.ApplicationListUiStates
import com.example.vp_alp_karep_frontend.views.templates.ApplicationCards

private val PrimaryTeal = Color(0xFF1A4D56)      // Balanced teal
private val AccentGold = Color(0xFFD4AF37)       // Brighter gold
private val LightGold = Color(0xFFF0E5C9)        // Softer light gold
private val DarkTeal = Color(0xFF0F2F35)         // Soft dark teal
private val BackgroundDark = Color(0xFF0A2026)   // Comfortable dark background
private val CardBackground = Color(0xFF1E3A41)   // Balanced card background
private val SecondaryTeal = Color(0xFF2A5F69)

@Composable
fun ApplicationScreen(
    viewModel: ApplicationListViewModel = viewModel(factory = ApplicationListViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadApplications()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    when(state) {
        is ApplicationListUiStates.Start -> {}
        is ApplicationListUiStates.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                BackgroundDark,
                                DarkTeal,
                                PrimaryTeal.copy(alpha = 0.4f),
                                SecondaryTeal.copy(alpha = 0.2f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        is ApplicationListUiStates.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text((state as ApplicationListUiStates.Error).message)
            }
        }
        is ApplicationListUiStates.Success -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                BackgroundDark,
                                DarkTeal,
                                PrimaryTeal.copy(alpha = 0.4f),
                                SecondaryTeal.copy(alpha = 0.2f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            ) {
                if (((state as ApplicationListUiStates.Success).applications).isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No applications found.",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Column {
                    Text(
                        text = "Your Applications",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                    ApplicationCards(
                        onCancel = { id ->
                            viewModel.cancelApplication(id)
                        },
                        onDelete = { id ->
                            viewModel.deleteApplication(id)
                        },
                        applications = (state as ApplicationListUiStates.Success).applications
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ApplicationScreenPreview() {
    ApplicationScreen()
}