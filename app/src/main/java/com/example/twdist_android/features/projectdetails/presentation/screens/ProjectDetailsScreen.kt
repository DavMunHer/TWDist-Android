package com.example.twdist_android.features.projectdetails.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twdist_android.features.projectdetails.presentation.components.SectionsRow
import com.example.twdist_android.features.projectdetails.presentation.components.dialogs.ProjectDetailsDialogs
import com.example.twdist_android.features.projectdetails.presentation.event.ProjectEvent
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.event.TaskEvent
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.ProjectDetailsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProjectDetailsScreen(
    projectId: Long,
    onProjectDeleted: () -> Unit = {},
    onTaskClicked: (projectId: Long, sectionId: Long, taskId: Long) -> Unit = { _, _, _ -> },
    viewModel: ProjectDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(projectId) {
        viewModel.loadProjectDetails(projectId)
    }

    LaunchedEffect(uiState.projectDeleted) {
        if (uiState.projectDeleted) {
            onProjectDeleted()
            viewModel.onProjectEvent(ProjectEvent.DeletedHandled)
        }
    }

    LaunchedEffect(uiState.taskCompletionUndo) {
        val pendingUndo = uiState.taskCompletionUndo ?: return@LaunchedEffect
        val autoDismissJob = launch {
            delay(3_000)
            snackbarHostState.currentSnackbarData?.dismiss()
        }
        val result = snackbarHostState.showSnackbar(
            message = "Task was marked as complete",
            actionLabel = "Undo",
            duration = SnackbarDuration.Indefinite
        )
        autoDismissJob.cancel()
        viewModel.onTaskEvent(TaskEvent.TaskCompletionUndoHandled(undo = result == SnackbarResult.ActionPerformed))
    }

    LaunchedEffect(uiState.taskSnackbarMessage) {
        val message = uiState.taskSnackbarMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        viewModel.onTaskEvent(TaskEvent.TaskSnackbarShown)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingState()
            uiState.error != null -> ErrorState(
                message = uiState.error!!,
                onRetry = viewModel::retry
            )
            uiState.project != null -> ProjectDetailsContent(
                uiState = uiState,
                onSectionEvent = viewModel::onSectionEvent,
                onTaskEvent = viewModel::onTaskEvent,
                onProjectEvent = viewModel::onProjectEvent,
                onTaskClick = { sectionId, taskId ->
                    onTaskClicked(projectId, sectionId, taskId)
                }
            )
            else -> MissingProjectDetails()
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
    modifier: Modifier = Modifier,
    uiState: ProjectDetailsUiState,
    onSectionEvent: (SectionEvent) -> Unit,
    onTaskEvent: (TaskEvent) -> Unit,
    onProjectEvent: (ProjectEvent) -> Unit,
    onTaskClick: (sectionId: Long, taskId: Long) -> Unit
) {
    val project = uiState.project ?: return
    Column(
        modifier = modifier
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

        SectionsRow(
            modifier = Modifier.weight(1f),
            uiState = uiState,
            onSectionEvent = onSectionEvent,
            onTaskEvent = onTaskEvent,
            onTaskClick = onTaskClick
        )

        ProjectDetailsDialogs(
            uiState = uiState,
            onSectionEvent = onSectionEvent,
            onTaskEvent = onTaskEvent,
            onProjectEvent = onProjectEvent
        )
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
