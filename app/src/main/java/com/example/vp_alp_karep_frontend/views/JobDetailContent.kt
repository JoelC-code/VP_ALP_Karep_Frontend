package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vp_alp_karep_frontend.models.JobModel
import com.example.vp_alp_karep_frontend.models.JobTagModel

private val PrimaryTeal = Color(0xFF1A4D56)      // Balanced teal
private val AccentGold = Color(0xFFD4AF37)       // Brighter gold
private val LightGold = Color(0xFFF0E5C9)        // Softer light gold
private val DarkTeal = Color(0xFF0F2F35)         // Soft dark teal
private val BackgroundDark = Color(0xFF0A2026)   // Comfortable dark background
private val CardBackground = Color(0xFF1E3A41)   // Balanced card background
private val SecondaryTeal = Color(0xFF2A5F69)

@Composable
fun JobDetailContent(
    job: JobModel,
    onApplyClick: () -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp).background(color = CardBackground, shape = RoundedCornerShape(8.dp))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = job.name,
                color = AccentGold,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                job.tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.1f)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "• ${tag.name}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Company",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = job.description,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(32.dp))

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Button(
                onClick = onApplyClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentGold,
                    contentColor = DarkTeal
                )
            ) {
                Text("Apply Pekerjaan")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CardBackground,
                    contentColor = AccentGold
                ),
                border = BorderStroke(1.dp, AccentGold)
            ) {
                Text("Kembali")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JobDetailContentPreview() {
    val fakeJob = JobModel(
        id = 1,
        name = "Android Developer",
        description = "Develop modern Android applications using Kotlin, Jetpack Compose, and MVVM architecture. Collaborate with backend teams and designers.",
        tags = listOf(
            JobTagModel(1, "Kotlin"),
            JobTagModel(2, "Jetpack Compose"),
            JobTagModel(3, "MVVM")
        )
    )

    MaterialTheme {
        JobDetailContent(
            job = fakeJob,
            onApplyClick = {},
            onBack = {},
        )
    }
}
