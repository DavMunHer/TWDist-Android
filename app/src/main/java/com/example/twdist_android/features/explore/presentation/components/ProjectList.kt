package com.example.twdist_android.features.explore.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProjectList(
    projects: List<ProjectItem>,
    onProjectClick: (ProjectItem) -> Unit,
    onStarClick: (ProjectItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        projects.forEach { project ->
            ProjectCard(
                project = project,
                onStarClick = { onStarClick(project) },
                onProjectClick = { onProjectClick(project) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
