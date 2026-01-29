package com.example.twdist_android.features.explore.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.explore.presentation.components.ProjectItem
import com.example.twdist_android.features.explore.presentation.components.ProjectList
import com.example.twdist_android.features.explore.presentation.components.SectionHeader

@Composable
fun ExploreScreen() {
    var isExpanded by remember { mutableStateOf(true) }
    val projects = remember {
        listOf(
            ProjectItem("Project Alpha", 5),
            ProjectItem("Project Beta", 12),
            ProjectItem("Project Gamma", 3),
            ProjectItem("Project Delta", 8)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        item {
            SectionHeader(
                title = "My projects",
                isExpanded = isExpanded,
                onExpandClick = { isExpanded = !isExpanded },
                onAddClick = { /* TODO: Logic for createing a project */ }
            )
        }

        if (isExpanded) {
            item {
                ProjectList(
                    projects = projects,
                    onProjectClick = { project -> /* TODO: Redirect to project view */ },
                    onStarClick = { project -> /* TODO: Add a project to favourite */ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    ExploreScreen()
}
