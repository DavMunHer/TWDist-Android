package com.example.twdist_android.features.projectdetails.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.event.TaskEvent
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState
import com.example.twdist_android.features.projectdetails.presentation.model.SectionUi

@Composable
internal fun SectionCard(
    section: SectionUi,
    uiState: ProjectDetailsUiState,
    onSectionEvent: (SectionEvent) -> Unit,
    onTaskEvent: (TaskEvent) -> Unit,
    onTaskClick: (sectionId: Long, taskId: Long) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionHeader(
            section = section,
            openSectionMenuForId = uiState.openSectionMenuForId,
            onSectionEvent = onSectionEvent
        )

        val sectionTasks = section.taskIds.mapNotNull { uiState.tasksById[it] }
        sectionTasks.forEach { taskItem ->
            TaskCard(
                sectionId = section.id,
                taskItem = taskItem,
                isTaskMenuOpen = uiState.openTaskMenuForId == taskItem.id,
                onTaskEvent = onTaskEvent,
                onTaskClick = onTaskClick
            )
        }

        AddTaskCard(
            sectionId = section.id,
            onTaskEvent = onTaskEvent
        )
    }
}
