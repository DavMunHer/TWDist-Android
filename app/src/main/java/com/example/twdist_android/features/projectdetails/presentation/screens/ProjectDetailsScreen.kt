package com.example.twdist_android.features.projectdetails.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.ProjectDetailsViewModel

@Composable
fun ProjectDetailsScreen(
    projectId: Long,
    viewModel: ProjectDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(projectId) {
        viewModel.loadProjectDetails(projectId)
    }

    when {
        uiState.isLoading -> LoadingState()
        uiState.error != null -> ErrorState(
            message = uiState.error!!,
            onRetry = viewModel::retry
        )
        uiState.project != null -> ProjectDetailsContent(
            uiState = uiState,
            onSectionOptionsClick = viewModel::onSectionOptionsClick,
            onSectionOptionsDismiss = viewModel::onSectionOptionsDismiss,
            onEditSectionClick = viewModel::onEditSectionClick,
            onDeleteSectionClick = viewModel::onDeleteSectionClick,
            onEditSectionDismiss = viewModel::onEditSectionDismiss,
            onEditSectionNameChange = viewModel::onEditSectionNameChange,
            onSaveSectionEdit = viewModel::onSaveSectionEdit,
            onDeleteSectionDismiss = viewModel::onDeleteSectionDismiss,
            onDeleteSectionConfirm = viewModel::onDeleteSectionConfirm
        )
        else -> MissingProjectDetails()
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun ProjectDetailsContent(
    uiState: ProjectDetailsUiState,
    onSectionOptionsClick: (Long) -> Unit,
    onSectionOptionsDismiss: () -> Unit,
    onEditSectionClick: (Long) -> Unit,
    onDeleteSectionClick: (Long) -> Unit,
    onEditSectionDismiss: () -> Unit,
    onEditSectionNameChange: (String) -> Unit,
    onSaveSectionEdit: () -> Unit,
    onDeleteSectionDismiss: () -> Unit,
    onDeleteSectionConfirm: () -> Unit
) {
    val project = uiState.project ?: return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "Options"
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )
        if (uiState.sectionActionError != null) {
            Text(
                text = uiState.sectionActionError,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(project.sections) { section ->
                Column(
                    modifier = Modifier.fillParentMaxWidth(0.95f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Section Header
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = section.name,
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                                IconButton(onClick = { onSectionOptionsClick(section.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreHoriz,
                                        contentDescription = "Section Options",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = uiState.openSectionMenuForId == section.id,
                                    onDismissRequest = onSectionOptionsDismiss
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "Edit") },
                                        onClick = { onEditSectionClick(section.id) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "Delete") },
                                        onClick = { onDeleteSectionClick(section.id) }
                                    )
                                }
                            }
                        }
                    }

                    // Tasks List
                    section.tasks.forEach { taskItem ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Circle,
                                    contentDescription = "Unchecked",
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = taskItem,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }

                    // Add Task Button
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(MaterialTheme.colorScheme.surface, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Add task",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }
            }
        }

        if (uiState.editingSectionId != null) {
            AlertDialog(
                onDismissRequest = onEditSectionDismiss,
                title = { Text("Edit Section") },
                text = {
                    TextField(
                        value = uiState.editingSectionName,
                        onValueChange = onEditSectionNameChange,
                        placeholder = { Text("Section name") },
                        singleLine = true,
                        isError = uiState.sectionActionError != null,
                        supportingText = uiState.sectionActionError?.let {
                            { Text(text = it, color = MaterialTheme.colorScheme.error) }
                        }
                    )
                },
                confirmButton = {
                    Button(onClick = onSaveSectionEdit) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onEditSectionDismiss) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (uiState.deleteConfirmSectionId != null) {
            AlertDialog(
                onDismissRequest = onDeleteSectionDismiss,
                title = { Text("Delete Section") },
                text = { Text("Are you sure you want to delete this section?") },
                confirmButton = {
                    Button(onClick = onDeleteSectionConfirm) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDeleteSectionDismiss) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun MissingProjectDetails() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Project not found",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "No data is available for this project.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
