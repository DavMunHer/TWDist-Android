package com.example.twdist_android.features.taskdetails.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twdist_android.features.taskdetails.presentation.components.DateInputField
import com.example.twdist_android.features.taskdetails.presentation.components.ErrorState
import com.example.twdist_android.features.taskdetails.presentation.components.LoadingState
import com.example.twdist_android.features.taskdetails.presentation.components.TaskDetailsHeaderCard
import com.example.twdist_android.features.taskdetails.presentation.event.TaskDetailsEvent
import com.example.twdist_android.features.taskdetails.presentation.model.TaskDetailsUi
import com.example.twdist_android.features.taskdetails.presentation.model.TaskDetailsUiEvent
import com.example.twdist_android.features.taskdetails.presentation.viewmodel.TaskDetailsViewModel

@Composable
fun TaskDetailsScreen(
    projectId: Long,
    sectionId: Long,
    taskId: Long,
    onNavigateBackToProjectDetails: () -> Unit = {},
    viewModel: TaskDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTask = uiState.task

    LaunchedEffect(projectId, sectionId, taskId) {
        viewModel.loadTaskDetails(projectId = projectId, sectionId = sectionId, taskId = taskId)
    }
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                TaskDetailsUiEvent.NavigateBackToProjectDetails -> onNavigateBackToProjectDetails()
            }
        }
    }

    when {
        uiState.isLoading -> LoadingState()
        uiState.error != null -> ErrorState(message = uiState.error!!)
        selectedTask != null -> TaskDetailsContent(
            projectName = uiState.projectName,
            sectionId = sectionId,
            task = selectedTask,
            isMenuOpen = uiState.openProjectMenu,
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            description = uiState.description,
            isSaving = uiState.isSaving,
            saveMessage = uiState.saveMessage,
            openTaskMenuForId = uiState.openTaskMenuForId,
            editingTaskId = uiState.editingTaskId,
            editingTaskName = uiState.editingTaskName,
            deleteConfirmTaskId = uiState.deleteConfirmTaskId,
            taskActionError = uiState.taskActionError,
            isTaskEditLoading = uiState.isTaskEditLoading,
            isTaskDeleteLoading = uiState.isTaskDeleteLoading,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun TaskDetailsContent(
    projectName: String,
    sectionId: Long,
    task: TaskDetailsUi,
    isMenuOpen: Boolean,
    startDate: String,
    endDate: String,
    description: String,
    isSaving: Boolean,
    saveMessage: String?,
    openTaskMenuForId: Long?,
    editingTaskId: Long?,
    editingTaskName: String,
    deleteConfirmTaskId: Long?,
    taskActionError: String?,
    isTaskEditLoading: Boolean,
    isTaskDeleteLoading: Boolean,
    onEvent: (TaskDetailsEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = projectName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Box {
                IconButton(onClick = { onEvent(TaskDetailsEvent.MenuOpened) }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Project Options"
                    )
                }
                DropdownMenu(
                    expanded = isMenuOpen,
                    onDismissRequest = { onEvent(TaskDetailsEvent.MenuDismissed) }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = { onEvent(TaskDetailsEvent.MenuEditClicked) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = { onEvent(TaskDetailsEvent.MenuDeleteClicked) }
                    )
                }
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )

        TaskDetailsHeaderCard(
            sectionId = sectionId,
            task = task,
            isTaskMenuOpen = openTaskMenuForId == task.id,
            onEvent = onEvent
        )

        OutlinedTextField(
            value = description,
            onValueChange = { onEvent(TaskDetailsEvent.DescriptionChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            label = { Text("Description") }
        )

        DateInputField(
            label = "Start Date",
            value = startDate,
            onValueChange = { onEvent(TaskDetailsEvent.StartDateChanged(it)) }
        )

        DateInputField(
            label = "End Date",
            value = endDate,
            onValueChange = { onEvent(TaskDetailsEvent.EndDateChanged(it)) }
        )

        Button(
            onClick = { onEvent(TaskDetailsEvent.SaveClicked) },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isSaving) "Saving..." else "Save Changes")
        }

        if (!saveMessage.isNullOrBlank()) {
            Text(
                text = saveMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (!taskActionError.isNullOrBlank()) {
            Text(
                text = taskActionError,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {},
            enabled = false
        ) {
            Text(text = "Add Subtask")
        }

        if (editingTaskId != null) {
            AlertDialog(
                onDismissRequest = { onEvent(TaskDetailsEvent.TaskEditDismissed) },
                title = { Text("Edit Task") },
                text = {
                    OutlinedTextField(
                        value = editingTaskName,
                        onValueChange = { onEvent(TaskDetailsEvent.TaskEditNameChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Task name") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { onEvent(TaskDetailsEvent.TaskEditConfirmed) },
                        enabled = !isTaskEditLoading
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(TaskDetailsEvent.TaskEditDismissed) }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (deleteConfirmTaskId != null) {
            AlertDialog(
                onDismissRequest = { onEvent(TaskDetailsEvent.TaskDeleteDismissed) },
                title = { Text("Delete Task") },
                text = { Text("Are you sure you want to delete this task?") },
                confirmButton = {
                    Button(
                        onClick = { onEvent(TaskDetailsEvent.TaskDeleteConfirmed) },
                        enabled = !isTaskDeleteLoading
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(TaskDetailsEvent.TaskDeleteDismissed) }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

