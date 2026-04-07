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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twdist_android.features.projectdetails.presentation.components.SectionsRow
import com.example.twdist_android.features.projectdetails.presentation.components.dialogs.ProjectDetailsDialogs
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

        SectionsRow(
            uiState = uiState,
            onSectionEvent = onSectionEvent
        )

        ProjectDetailsDialogs(
            uiState = uiState,
            onSectionEvent = onSectionEvent,
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
