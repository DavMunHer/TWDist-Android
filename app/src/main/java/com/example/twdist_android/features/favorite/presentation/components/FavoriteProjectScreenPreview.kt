package com.example.twdist_android.features.favorite.presentation.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.twdist_android.core.ui.theme.TWDistAndroidTheme
import com.example.twdist_android.features.explore.presentation.model.ProjectUi
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectsUiState

private fun sampleFavoriteProjects(): List<ProjectUi> = listOf(
    ProjectUi(id = 1L, name = "University", isFavorite = true, pendingTasks = 3),
    ProjectUi(id = 2L, name = "Personal", isFavorite = true, pendingTasks = 0)
)

@Preview(showBackground = true, name = "With projects")
@Composable
internal fun FavoriteProjectScreenContentPreview() {
    TWDistAndroidTheme {
        FavoriteProjectScreenContent(
            uiState = FavoriteProjectsUiState(projects = sampleFavoriteProjects()),
            snackbarHostState = remember { SnackbarHostState() },
            onProjectClick = {},
            onRetry = {},
            onUnfavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
internal fun FavoriteProjectScreenLoadingPreview() {
    TWDistAndroidTheme {
        FavoriteProjectScreenContent(
            uiState = FavoriteProjectsUiState(isLoading = true),
            snackbarHostState = remember { SnackbarHostState() },
            onProjectClick = {},
            onRetry = {},
            onUnfavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
internal fun FavoriteProjectScreenErrorPreview() {
    TWDistAndroidTheme {
        FavoriteProjectScreenContent(
            uiState = FavoriteProjectsUiState(
                isLoading = false,
                error = "Could not load favorite projects"
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onProjectClick = {},
            onRetry = {},
            onUnfavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
internal fun FavoriteProjectScreenEmptyPreview() {
    TWDistAndroidTheme {
        FavoriteProjectScreenContent(
            uiState = FavoriteProjectsUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            onProjectClick = {},
            onRetry = {},
            onUnfavoriteClick = {}
        )
    }
}
