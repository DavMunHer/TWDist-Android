package com.example.twdist_android.features.favorite.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twdist_android.features.explore.presentation.components.ProjectListRecycler
import com.example.twdist_android.features.explore.presentation.components.SectionHeader
import com.example.twdist_android.features.favorite.presentation.event.FavoriteProjectsEvent
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectsUiState
import com.example.twdist_android.features.favorite.presentation.viewmodel.FavoriteProjectsViewModel

@Composable
fun FavoriteProjectScreen(
    onNavigateToProjectDetails: (Long) -> Unit = {},
    viewModel: FavoriteProjectsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.handleEvent(FavoriteProjectsEvent.LoadProjects)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(uiState.pendingUndo) {
        val undoAction = uiState.pendingUndo ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = "Removed from favorites",
            actionLabel = "Undo",
            duration = SnackbarDuration.Short
        )
        if (result == SnackbarResult.ActionPerformed) {
            viewModel.handleEvent(FavoriteProjectsEvent.UndoUnfavorite(undoAction.token))
        } else {
            viewModel.handleEvent(FavoriteProjectsEvent.UndoMessageHandled(undoAction.token))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading && uiState.projects.isEmpty() -> LoadingState()
            uiState.error != null && uiState.projects.isEmpty() -> ErrorState(
                message = uiState.error ?: "Unknown error",
                onRetry = { viewModel.handleEvent(FavoriteProjectsEvent.LoadProjects) }
            )
            uiState.projects.isEmpty() -> EmptyState()
            else -> FavoriteContent(
                uiState = uiState,
                onProjectClick = onNavigateToProjectDetails,
                onUnfavoriteClick = { projectId ->
                    viewModel.handleEvent(FavoriteProjectsEvent.UnfavoriteProject(projectId))
                }
            )
        }

        uiState.error?.let { message ->
            if (uiState.projects.isNotEmpty()) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun FavoriteContent(
    uiState: FavoriteProjectsUiState,
    onProjectClick: (Long) -> Unit,
    onUnfavoriteClick: (Long) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Favorite projects",
            isExpanded = isExpanded,
            onExpandClick = { isExpanded = !isExpanded },
            onAddClick = {},
            showAddButton = false,
            modifier = Modifier.fillMaxWidth()
        )

        if (isExpanded) {
            ProjectListRecycler(
                projects = uiState.projects,
                onProjectClick = { project -> onProjectClick(project.id) },
                onStarClick = { project -> onUnfavoriteClick(project.id) },
                onSwipeDeleteThreshold = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
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
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Favorite projects",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "You have no favorite projects yet.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteProjectScreenPreview() {
    FavoriteContent(
        uiState = FavoriteProjectsUiState(),
        onProjectClick = {},
        onUnfavoriteClick = {}
    )
}