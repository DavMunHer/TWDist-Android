package com.example.twdist_android.features.explore.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.twdist_android.features.explore.presentation.components.recyclerview.ProjectListRecyclerAdapter
import com.example.twdist_android.features.explore.presentation.components.recyclerview.ProjectRowColors
import com.example.twdist_android.features.explore.presentation.components.recyclerview.ProjectSwipeDeleteCallback
import com.example.twdist_android.features.explore.presentation.model.ProjectUi

private class ProjectListRecyclerInteraction {
    var onProjectClick: (ProjectUi) -> Unit = {}
    var onStarClick: (ProjectUi) -> Unit = {}
    var onSwipeDeleteThreshold: (Long) -> Unit = {}
}

@Composable
fun ProjectListRecycler(
    projects: List<ProjectUi>,
    onProjectClick: (ProjectUi) -> Unit,
    onStarClick: (ProjectUi) -> Unit,
    onSwipeDeleteThreshold: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val deleteLabel = "Delete"
    val scheme = MaterialTheme.colorScheme
    val rowColors = ProjectRowColors(
        surfaceVariant = scheme.surfaceVariant.toArgb(),
        primary = scheme.primary.toArgb(),
        onSurface = scheme.onSurface.toArgb()
    )
    val interaction = remember { ProjectListRecyclerInteraction() }
    SideEffect {
        interaction.onProjectClick = onProjectClick
        interaction.onStarClick = onStarClick
        interaction.onSwipeDeleteThreshold = onSwipeDeleteThreshold
    }
    val adapter = remember {
        ProjectListRecyclerAdapter().also { listAdapter ->
            listAdapter.onProjectClick = { interaction.onProjectClick(it) }
            listAdapter.onStarClick = { interaction.onStarClick(it) }
        }
    }
    val swipeCallback = remember {
        ProjectSwipeDeleteCallback(
            adapter = adapter,
            deleteSwipeLabel = "",
            onSwipeDeleteThreshold = { id -> interaction.onSwipeDeleteThreshold(id) }
        )
    }
    AndroidView(
        factory = { context ->
            RecyclerView(context).apply {
                layoutManager = LinearLayoutManager(context)
                this.adapter = adapter
                ItemTouchHelper(swipeCallback).attachToRecyclerView(this)
            }
        },
        update = {
            swipeCallback.deleteSwipeLabel = deleteLabel
            adapter.syncListAndColors(projects, rowColors)
        },
        modifier = modifier
    )
}
