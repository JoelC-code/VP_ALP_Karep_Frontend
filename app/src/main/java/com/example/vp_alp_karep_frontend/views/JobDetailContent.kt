package com.example.vp_alp_karep_frontend.views

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

@Composable
fun JobDetailContent(
    job: JobModel,
    onApplyClick: () -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = job.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Company",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = job.description,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                job.tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(12.dp),
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF42A5F5), Color(0xFF1E88E5))
                                )
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tag.name,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                }
            }

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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply Job")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
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
