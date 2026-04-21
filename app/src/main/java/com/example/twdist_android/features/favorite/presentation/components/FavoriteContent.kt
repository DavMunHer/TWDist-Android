package com.example.twdist_android.features.favorite.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.explore.presentation.components.ProjectListRecycler
import com.example.twdist_android.features.explore.presentation.components.SectionHeader
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectsUiState

@Composable
internal fun FavoriteContent(
    uiState: FavoriteProjectsUiState,
    onProjectClick: (Long) -> Unit,
    onUnfavoriteClick: (Long) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Favorite projects",
            isExpanded = isExpanded,
            onExpandClick = { isExpanded = !isExpanded },
            onAddClick = {},
            showAddButton = false,
            modifier = Modifier.fillMaxWidth()
        )

        if (isExpanded) {
            ProjectListRecycler(
                projects = uiState.projects,
                onProjectClick = { project -> onProjectClick(project.id) },
                onStarClick = { project -> onUnfavoriteClick(project.id) },
                onSwipeDeleteThreshold = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
