package com.example.vp_alp_karep_frontend.views.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vp_alp_karep_frontend.enums.ApplicationStatus
import com.example.vp_alp_karep_frontend.enums.displayText
import com.example.vp_alp_karep_frontend.models.ApplicationModel
import com.example.vp_alp_karep_frontend.models.JobModel
import com.example.vp_alp_karep_frontend.models.JobTagModel

private val CardBackground = Color(0xFF1E3A41)

@Composable
fun ApplicationCards(
    onCancel: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    applications: List<ApplicationModel>
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(applications) { app ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Job Name
                    Text(
                        text = app.job!!.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )

                    // Company Placeholder
                    Text(
                        text = "Company",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Status
                    Text(
                        text = "Status: ${app.status.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cancel/Delete buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (app.status == ApplicationStatus.Pending) {
                            Button(
                                onClick = { onCancel(app.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cancel"
                                )
                            }
                        }
                        if (app.status == ApplicationStatus.Rejected || app.status == ApplicationStatus.Cancelled) {
                            Button(
                                onClick = { onDelete(app.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                        if (app.status == ApplicationStatus.Accepted || app.status == ApplicationStatus.Accepted) {
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF36963A),
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApplicationContentPreview() {
    val fakeApps = listOf(
        ApplicationModel(
            id = 1,
            status = ApplicationStatus.Pending,
            job = JobModel(
                id = 1,
                name = "Android Developer",
                description = "Build Android apps",
                tags = listOf(
                    JobTagModel(1, "Kotlin"),
                    JobTagModel(2, "Compose")
                )
            )
        ),
        ApplicationModel(
            id = 2,
            status = ApplicationStatus.Rejected,
            job = JobModel(
                id = 2,
                name = "Backend Engineer",
                description = "Node & PostgreSQL",
                tags = listOf(
                    JobTagModel(3, "Node.js"),
                    JobTagModel(4, "PostgreSQL")
                )
            )
        ),
        ApplicationModel(
            id = 3,
            status = ApplicationStatus.Accepted,
            job = JobModel(
                id = 3,
                name = "UI/UX Designer",
                description = "Design systems",
                tags = listOf(
                    JobTagModel(5, "Figma"),
                    JobTagModel(6, "UX")
                )
            )
        )
    )

    ApplicationCards(
        applications = fakeApps,
        onCancel = {},
        onDelete = {}
    )
}