package com.example.twdist_android.features.explore.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twdist_android.features.explore.presentation.components.CreateProjectDialog
import com.example.twdist_android.features.explore.presentation.components.ProjectList
import com.example.twdist_android.features.explore.presentation.components.SectionHeader
import com.example.twdist_android.features.explore.presentation.event.ExploreEvent
import com.example.twdist_android.features.explore.presentation.viewmodel.ExploreViewModel

@Composable
fun ExploreScreen(
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

    // Dialog states
    var showCreateDialog by remember { mutableStateOf(false) }

    // Close dialog when project creation succeeds (projects list changes and no errors)
    LaunchedEffect(state.projects.size, state.projectNameError, state.isLoading) {
        if (showCreateDialog && !state.isLoading && state.projectNameError == null && state.error == null) {
            // If we were showing dialog, not loading, and no errors, assume success
            showCreateDialog = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            item {
                SectionHeader(
                    title = "My projects",
                    isExpanded = state.isExpanded,
                    onExpandClick = { viewModel.handleEvent(ExploreEvent.ToggleExpanded) },
                    onAddClick = { showCreateDialog = true }
                )
            }

            if (state.isExpanded) {
                // If its loading and no projects, show loading indicator
                if (state.isLoading && state.projects.isEmpty()) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                                .wrapContentWidth()
                        )
                    }
                }

                item {
                    ProjectList(
                        projects = state.projects,
                        onProjectClick = { project -> onNavigateToProjectDetails(project.id) },
                        onStarClick = { _ -> /* TODO: Logic add favourite */ }
                    )
                }
            }
        }

        // Show error message if exists (for business logic errors, not validation)
        state.error?.let { msg ->
            Text(
                text = msg,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    // Create Project Dialog
    if (showCreateDialog) {
        CreateProjectDialog(
            onDismiss = {
                showCreateDialog = false
                // Clear any validation errors when dismissing
                viewModel.handleEvent(ExploreEvent.ClearValidationErrors)
            },
            onConfirm = { name -> viewModel.handleEvent(ExploreEvent.CreateProject(name)) },
            error = state.projectNameError
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    ExploreScreen()
}
