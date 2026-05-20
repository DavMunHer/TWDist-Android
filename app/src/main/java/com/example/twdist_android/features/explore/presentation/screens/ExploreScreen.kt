package com.example.twdist_android.features.explore.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.twdist_android.core.ui.components.ScreenHeader
import com.example.twdist_android.core.ui.theme.TWDistAndroidTheme
import com.example.twdist_android.features.explore.presentation.components.CreateProjectDialog
import com.example.twdist_android.features.explore.presentation.components.DeleteProjectDialog
import com.example.twdist_android.features.explore.presentation.components.ProjectListRecycler
import com.example.twdist_android.features.explore.presentation.components.SectionHeader
import com.example.twdist_android.features.explore.presentation.event.ExploreEvent
import com.example.twdist_android.features.explore.presentation.model.ExploreUiState
import com.example.twdist_android.features.explore.presentation.model.ProjectUi
import com.example.twdist_android.features.explore.presentation.viewmodel.ExploreViewModel

@Composable
fun ExploreScreen(
    onLogout: () -> Unit,
    isLoggingOut: Boolean = false,
    onNavigateToProjectDetails: (Long) -> Unit = {},
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.handleEvent(ExploreEvent.LoadProjects)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.projects.size, state.projectNameError, state.isLoading) {
        if (showCreateDialog && !state.isLoading && state.projectNameError == null && state.error == null) {
            showCreateDialog = false
        }
    }

    ExploreScreenContent(
        state = state,
        onNavigateToProjectDetails = onNavigateToProjectDetails,
        onToggleExpanded = { viewModel.handleEvent(ExploreEvent.ToggleExpanded) },
        onAddClick = { showCreateDialog = true },
        onToggleFavorite = { projectId ->
            viewModel.handleEvent(ExploreEvent.ToggleProjectFavorite(projectId))
        },
        onSwipeDelete = { projectId ->
            viewModel.handleEvent(ExploreEvent.ShowDeleteProjectConfirmation(projectId))
        },
        onLogout = onLogout,
        isLoggingOut = isLoggingOut
    )

    if (showCreateDialog) {
        CreateProjectDialog(
            onDismiss = {
                showCreateDialog = false
                viewModel.handleEvent(ExploreEvent.ClearValidationErrors)
            },
            onConfirm = { name -> viewModel.handleEvent(ExploreEvent.CreateProject(name)) },
            error = state.projectNameError
        )
    }

    state.projectPendingDelete?.let { project ->
        DeleteProjectDialog(
            project = project,
            onDismiss = { viewModel.handleEvent(ExploreEvent.DismissDeleteProjectConfirmation) },
            onConfirm = { viewModel.handleEvent(ExploreEvent.ConfirmDeleteProject) }
        )
    }
}

@Composable
fun ExploreScreenContent(
    state: ExploreUiState,
    onNavigateToProjectDetails: (Long) -> Unit,
    onToggleExpanded: () -> Unit,
    onAddClick: () -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onSwipeDelete: (Long) -> Unit,
    onLogout: () -> Unit,
    isLoggingOut: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
        ) {
            ScreenHeader(
                title = "Explore",
                onLogout = onLogout,
                isLoggingOut = isLoggingOut
            )
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = "My projects",
                isExpanded = state.isExpanded,
                onExpandClick = onToggleExpanded,
                onAddClick = onAddClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (state.isExpanded) {
                if (state.isLoading && state.projects.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    ProjectListRecycler(
                        projects = state.projects,
                        onProjectClick = { project -> onNavigateToProjectDetails(project.id) },
                        onStarClick = { project -> onToggleFavorite(project.id) },
                        onSwipeDeleteThreshold = onSwipeDelete,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }

        state.error?.let { msg ->
            Text(
                text = msg,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

private fun sampleExploreProjects(): List<ProjectUi> = listOf(
    ProjectUi(id = 1L, name = "University", isFavorite = true, pendingTasks = 3),
    ProjectUi(id = 2L, name = "Personal", isFavorite = false, pendingTasks = 1)
)

@Preview(showBackground = true)
@Composable
private fun ExploreScreenContentPreview() {
    TWDistAndroidTheme {
        ExploreScreenContent(
            state = ExploreUiState(projects = sampleExploreProjects()),
            onNavigateToProjectDetails = {},
            onToggleExpanded = {},
            onAddClick = {},
            onToggleFavorite = {},
            onSwipeDelete = {},
            onLogout = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreScreenLoadingPreview() {
    TWDistAndroidTheme {
        ExploreScreenContent(
            state = ExploreUiState(isLoading = true),
            onNavigateToProjectDetails = {},
            onToggleExpanded = {},
            onAddClick = {},
            onToggleFavorite = {},
            onSwipeDelete = {},
            onLogout = {}
        )
    }
}
