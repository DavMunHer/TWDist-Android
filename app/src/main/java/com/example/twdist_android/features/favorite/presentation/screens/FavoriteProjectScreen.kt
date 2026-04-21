package com.example.twdist_android.features.favorite.presentation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twdist_android.features.favorite.presentation.components.FavoriteProjectScreenContent
import com.example.twdist_android.features.favorite.presentation.components.FavoriteProjectsLifecycleEffect
import com.example.twdist_android.features.favorite.presentation.components.FavoriteUndoSnackbarEffect
import com.example.twdist_android.features.favorite.presentation.event.FavoriteProjectsEvent
import com.example.twdist_android.features.favorite.presentation.viewmodel.FavoriteProjectsViewModel

@Composable
fun FavoriteProjectScreen(
    onNavigateToProjectDetails: (Long) -> Unit = {},
    viewModel: FavoriteProjectsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }

    FavoriteProjectsLifecycleEffect(
        lifecycleOwner = lifecycleOwner,
        onLoadProjects = { viewModel.handleEvent(FavoriteProjectsEvent.LoadProjects) }
    )
    FavoriteUndoSnackbarEffect(
        pendingUndo = uiState.pendingUndo,
        snackbarHostState = snackbarHostState,
        onUndo = { token -> viewModel.handleEvent(FavoriteProjectsEvent.UndoUnfavorite(token)) },
        onUndoHandled = { token ->
            viewModel.handleEvent(FavoriteProjectsEvent.UndoMessageHandled(token))
        }
    )

    FavoriteProjectScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onProjectClick = onNavigateToProjectDetails,
        onRetry = { viewModel.handleEvent(FavoriteProjectsEvent.LoadProjects) },
        onUnfavoriteClick = { projectId ->
            viewModel.handleEvent(FavoriteProjectsEvent.UnfavoriteProject(projectId))
        }
    )
}