package com.example.twdist_android.features.favorite.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.explore.presentation.model.ProjectUi

@Composable
fun FavoriteProjectList(
    favoriteProjects: List<ProjectUi>,
    onProjectClick: (ProjectUi) -> Unit,
    onUnfavoriteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(favoriteProjects) { project ->
            FavoriteProjectCard(
                project = project,
                onProjectClick = onProjectClick,
                onUnfavoriteClick = onUnfavoriteClick
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Preview()
@Composable
fun FavoriteProjectListPreview() {
    val sampleProjects = listOf(
        ProjectUi(id = 1, name = "Project Alpha", pendingTasks = 5, isFavorite = true),
        ProjectUi(id = 2, name = "Project Beta", pendingTasks = 2, isFavorite = true),
        ProjectUi(id = 3, name = "Project Gamma", pendingTasks = 8, isFavorite = true)
    )
    FavoriteProjectList(
        favoriteProjects = sampleProjects,
        onProjectClick = {},
        onUnfavoriteClick = {}
    )
}