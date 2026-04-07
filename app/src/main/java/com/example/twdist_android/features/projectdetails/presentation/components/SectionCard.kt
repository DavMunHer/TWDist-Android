package com.example.twdist_android.features.projectdetails.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState
import com.example.twdist_android.features.projectdetails.presentation.model.SectionUi

@Composable
internal fun SectionCard(
    section: SectionUi,
    uiState: ProjectDetailsUiState,
    onSectionEvent: (SectionEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.95f),
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
                onSectionEvent = onSectionEvent
            )
        }

        AddTaskCard(
            sectionId = section.id,
            onSectionEvent = onSectionEvent
        )
    }
}
