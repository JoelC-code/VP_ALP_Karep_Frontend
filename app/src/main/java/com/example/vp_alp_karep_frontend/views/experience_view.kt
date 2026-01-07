package com.example.vp_alp_karep_frontend.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vp_alp_karep_frontend.models.Experience
import com.example.vp_alp_karep_frontend.viewModels.ExperienceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceView(
    viewModel: ExperienceViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedExperience by remember { mutableStateOf<Experience?>(null) }

    LaunchedEffect(uiState.isOperationSuccessful) {
        if (uiState.isOperationSuccessful) {
            showAddDialog = false
            showEditDialog = false
            selectedExperience = null
            viewModel.resetOperationStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Experience") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Experience")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.errorMessage!!,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadExperiences() }) {
                            Text("Coba Lagi")
                        }
                    }
                }
                uiState.experiences.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Belum ada experience",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tambahkan experience pertama Anda!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.experiences) { experience ->
                            ExperienceCard(
                                experience = experience,
                                onEdit = {
                                    selectedExperience = experience
                                    showEditDialog = true
                                },
                                onDelete = {
                                    experience.id?.let { viewModel.deleteExperience(it) }
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AddExperienceDialog(
                isLoading = uiState.isLoading,
                onDismiss = { showAddDialog = false },
                onSave = { title, description ->
                    viewModel.createExperience(title, description)
                }
            )
        }

        if (showEditDialog && selectedExperience != null) {
            EditExperienceDialog(
                experience = selectedExperience!!,
                isLoading = uiState.isLoading,
                onDismiss = {
                    showEditDialog = false
                    selectedExperience = null
                },
                onSave = { title, description ->
                    selectedExperience?.id?.let { id ->
                        viewModel.updateExperience(id, title, description)
                    }
                }
            )
        }
    }
}

@Composable
fun ExperienceCard(
    experience: Experience,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = experience.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            if (!experience.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = experience.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AddExperienceDialog(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Tambah Experience") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title *") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, description.ifBlank { null }) },
                enabled = !isLoading && title.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Simpan")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun EditExperienceDialog(
    experience: Experience,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String?) -> Unit
) {
    var title by remember { mutableStateOf(experience.title) }
    var description by remember { mutableStateOf(experience.description ?: "") }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Edit Experience") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title *") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, description.ifBlank { null }) },
                enabled = !isLoading && title.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Simpan")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text("Batal")
            }
        }
    )
}

