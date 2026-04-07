package com.example.twdist_android.features.projectdetails.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CheckCircle
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
import com.example.twdist_android.features.projectdetails.presentation.event.ProjectEvent
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.ProjectDetailsViewModel

@Composable
fun ProjectDetailsScreen(
    projectId: Long,
    onProjectDeleted: () -> Unit = {},
    viewModel: ProjectDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(projectId) {
        viewModel.loadProjectDetails(projectId)
    }

    LaunchedEffect(uiState.projectDeleted) {
        if (uiState.projectDeleted) {
            onProjectDeleted()
            viewModel.onProjectEvent(ProjectEvent.DeletedHandled)
        }
    }

    when {
        uiState.isLoading -> LoadingState()
        uiState.error != null -> ErrorState(
            message = uiState.error!!,
            onRetry = viewModel::retry
        )
        uiState.project != null -> ProjectDetailsContent(
            uiState = uiState,
            onSectionEvent = viewModel::onEvent,
            onProjectEvent = viewModel::onProjectEvent
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
    onSectionEvent: (SectionEvent) -> Unit,
    onProjectEvent: (ProjectEvent) -> Unit
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
            Box {
                IconButton(onClick = { onProjectEvent(ProjectEvent.MenuOpened) }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Project Options"
                    )
                }
                DropdownMenu(
                    expanded = uiState.openProjectMenu,
                    onDismissRequest = { onProjectEvent(ProjectEvent.MenuDismissed) }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Edit") },
                        onClick = { onProjectEvent(ProjectEvent.EditClicked) }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Delete") },
                        onClick = { onProjectEvent(ProjectEvent.DeleteClicked) }
                    )
                }
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )
        if (uiState.projectActionError != null) {
            Text(
                text = uiState.projectActionError,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        if (uiState.sectionActionError != null) {
            Text(
                text = uiState.sectionActionError,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        if (uiState.taskActionError != null) {
            Text(
                text = uiState.taskActionError,
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
                                IconButton(onClick = { onSectionEvent(SectionEvent.MenuOpened(section.id)) }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreHoriz,
                                        contentDescription = "Section Options",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = uiState.openSectionMenuForId == section.id,
                                    onDismissRequest = { onSectionEvent(SectionEvent.MenuDismissed) }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "Edit") },
                                        onClick = { onSectionEvent(SectionEvent.EditClicked(section.id)) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "Delete") },
                                        onClick = { onSectionEvent(SectionEvent.DeleteClicked(section.id)) }
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
                                    imageVector = if (taskItem.completed) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                                    contentDescription = "Task completion",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable {
                                            onSectionEvent(
                                                SectionEvent.TaskCompletionToggled(
                                                    sectionId = section.id,
                                                    taskId = taskItem.id
                                                )
                                            )
                                        },
                                    tint = if (taskItem.completed) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.outline
                                    }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = taskItem.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                Box {
                                    IconButton(onClick = {
                                        onSectionEvent(SectionEvent.TaskMenuOpened(taskItem.id))
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreHoriz,
                                            contentDescription = "Task options"
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = uiState.openTaskMenuForId == taskItem.id,
                                        onDismissRequest = { onSectionEvent(SectionEvent.TaskMenuDismissed) }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(text = "Edit") },
                                            onClick = {
                                                onSectionEvent(
                                                    SectionEvent.EditTaskClicked(
                                                        sectionId = section.id,
                                                        taskId = taskItem.id
                                                    )
                                                )
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(text = "Delete") },
                                            onClick = {
                                                onSectionEvent(
                                                    SectionEvent.DeleteTaskClicked(
                                                        sectionId = section.id,
                                                        taskId = taskItem.id
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Add Task Button
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSectionEvent(SectionEvent.AddTaskClicked(section.id)) },
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
                onDismissRequest = { onSectionEvent(SectionEvent.EditDismissed) },
                title = { Text("Edit Section") },
                text = {
                    TextField(
                        value = uiState.editingSectionName,
                        onValueChange = { onSectionEvent(SectionEvent.NameChanged(it)) },
                        placeholder = { Text("Section name") },
                        singleLine = true,
                        isError = uiState.sectionActionError != null,
                        supportingText = uiState.sectionActionError?.let {
                            { Text(text = it, color = MaterialTheme.colorScheme.error) }
                        }
                    )
                },
                confirmButton = {
                    Button(onClick = { onSectionEvent(SectionEvent.EditConfirmed) }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onSectionEvent(SectionEvent.EditDismissed) }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (uiState.creatingTaskSectionId != null) {
            AlertDialog(
                onDismissRequest = { onSectionEvent(SectionEvent.CreateTaskDismissed) },
                title = { Text("Add Task") },
                text = {
                    TextField(
                        value = uiState.creatingTaskName,
                        onValueChange = { onSectionEvent(SectionEvent.CreateTaskNameChanged(it)) },
                        placeholder = { Text("Task name") },
                        singleLine = true,
                        isError = uiState.taskActionError != null,
                        supportingText = uiState.taskActionError?.let {
                            { Text(text = it, color = MaterialTheme.colorScheme.error) }
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { onSectionEvent(SectionEvent.CreateTaskConfirmed) },
                        enabled = !uiState.isTaskCreateLoading
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onSectionEvent(SectionEvent.CreateTaskDismissed) }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (uiState.editingTaskId != null) {
            AlertDialog(
                onDismissRequest = { onSectionEvent(SectionEvent.EditTaskDismissed) },
                title = { Text("Edit Task") },
                text = {
                    TextField(
                        value = uiState.editingTaskName,
                        onValueChange = { onSectionEvent(SectionEvent.EditTaskNameChanged(it)) },
                        placeholder = { Text("Task name") },
                        singleLine = true,
                        isError = uiState.taskActionError != null,
                        supportingText = uiState.taskActionError?.let {
                            { Text(text = it, color = MaterialTheme.colorScheme.error) }
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { onSectionEvent(SectionEvent.EditTaskConfirmed) },
                        enabled = !uiState.isTaskEditLoading
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onSectionEvent(SectionEvent.EditTaskDismissed) }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (uiState.deleteConfirmTaskId != null) {
            AlertDialog(
                onDismissRequest = { onSectionEvent(SectionEvent.DeleteTaskDismissed) },
                title = { Text("Delete Task") },
                text = { Text("Are you sure you want to delete this task?") },
                confirmButton = {
                    Button(
                        onClick = { onSectionEvent(SectionEvent.DeleteTaskConfirmed) },
                        enabled = !uiState.isTaskDeleteLoading
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onSectionEvent(SectionEvent.DeleteTaskDismissed) }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (uiState.deleteConfirmSectionId != null) {
            AlertDialog(
                onDismissRequest = { onSectionEvent(SectionEvent.DeleteDismissed) },
                title = { Text("Delete Section") },
                text = { Text("Are you sure you want to delete this section?") },
                confirmButton = {
                    Button(onClick = { onSectionEvent(SectionEvent.DeleteConfirmed) }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onSectionEvent(SectionEvent.DeleteDismissed) }) {
                        Text("Cancel")
                    }
                }
            )
        }
        if (uiState.isEditingProject) {
            AlertDialog(
                onDismissRequest = { onProjectEvent(ProjectEvent.EditDismissed) },
                title = { Text("Edit Project") },
                text = {
                    TextField(
                        value = uiState.editingProjectName,
                        onValueChange = { onProjectEvent(ProjectEvent.NameChanged(it)) },
                        placeholder = { Text("Project name") },
                        singleLine = true,
                        isError = uiState.projectActionError != null,
                        supportingText = uiState.projectActionError?.let {
                            { Text(text = it, color = MaterialTheme.colorScheme.error) }
                        }
                    )
                },
                confirmButton = {
                    Button(onClick = { onProjectEvent(ProjectEvent.EditConfirmed) }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onProjectEvent(ProjectEvent.EditDismissed) }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (uiState.deleteConfirmProject) {
            AlertDialog(
                onDismissRequest = { onProjectEvent(ProjectEvent.DeleteDismissed) },
                title = { Text("Delete Project") },
                text = { Text("Are you sure you want to delete this project?") },
                confirmButton = {
                    Button(onClick = { onProjectEvent(ProjectEvent.DeleteConfirmed) }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onProjectEvent(ProjectEvent.DeleteDismissed) }) {
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
