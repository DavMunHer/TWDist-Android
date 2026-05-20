package com.example.twdist_android.features.favorite.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twdist_android.core.ui.components.ScreenHeader
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectsUiState

@Composable
internal fun FavoriteProjectScreenContent(
    uiState: FavoriteProjectsUiState,
    snackbarHostState: SnackbarHostState,
    onProjectClick: (Long) -> Unit,
    onRetry: () -> Unit,
    onUnfavoriteClick: (Long) -> Unit,
    onLogout: () -> Unit,
    isLoggingOut: Boolean = false
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            ScreenHeader(
                title = "Favorites",
                onLogout = onLogout,
                isLoggingOut = isLoggingOut
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.weight(1f)) {
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
            }
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
