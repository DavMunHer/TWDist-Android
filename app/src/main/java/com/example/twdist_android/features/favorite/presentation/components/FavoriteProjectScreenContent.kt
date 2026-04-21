package com.example.twdist_android.features.favorite.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectsUiState

@Composable
internal fun FavoriteProjectScreenContent(
    uiState: FavoriteProjectsUiState,
    snackbarHostState: SnackbarHostState,
    onProjectClick: (Long) -> Unit,
    onRetry: () -> Unit,
    onUnfavoriteClick: (Long) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading && uiState.projects.isEmpty() -> LoadingState()
            uiState.error != null && uiState.projects.isEmpty() -> ErrorState(
                message = uiState.error ?: "Unknown error",
                onRetry = onRetry
            )
            uiState.projects.isEmpty() -> EmptyState()
            else -> FavoriteContent(
                uiState = uiState,
                onProjectClick = onProjectClick,
                onUnfavoriteClick = onUnfavoriteClick
            )
        }

        uiState.error?.let { message ->
            if (uiState.projects.isNotEmpty()) {
                InlineErrorMessage(message = message)
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
