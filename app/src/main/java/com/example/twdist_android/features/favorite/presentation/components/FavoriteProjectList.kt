package com.example.twdist_android.features.favorite.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectState

@Composable
fun FavoriteProjectList(
    favoriteProjects: List<FavoriteProjectState>
) {
    LazyColumn {
        items(favoriteProjects) { project ->
            FavoriteProjectCard(project = project)
            Spacer(Modifier.height(15.dp))
        }
    }
}

@Preview()
@Composable
fun FavoriteProjectListPreview() {
    val sampleProjects = listOf(
        FavoriteProjectState("Project Alpha", 5),
        FavoriteProjectState("Project Beta", 2),
        FavoriteProjectState("Project Gamma", 8)
    )
    FavoriteProjectList(favoriteProjects = sampleProjects)
}