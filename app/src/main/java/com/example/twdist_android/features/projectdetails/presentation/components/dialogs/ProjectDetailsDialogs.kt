package com.example.twdist_android.features.projectdetails.presentation.components.dialogs

import androidx.compose.runtime.Composable
import com.example.twdist_android.features.projectdetails.presentation.event.ProjectEvent
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState

@Composable
fun ProjectDetailsDialogs(
    uiState: ProjectDetailsUiState,
    onSectionEvent: (SectionEvent) -> Unit,
    onProjectEvent: (ProjectEvent) -> Unit
) {
    if (uiState.editingSectionId != null) {
        EditSectionDialog(uiState = uiState, onSectionEvent = onSectionEvent)
    }

    if (uiState.creatingTaskSectionId != null) {
        CreateTaskDialog(uiState = uiState, onSectionEvent = onSectionEvent)
    }

    if (uiState.editingTaskId != null) {
        EditTaskDialog(uiState = uiState, onSectionEvent = onSectionEvent)
    }

    if (uiState.deleteConfirmTaskId != null) {
        DeleteTaskDialog(uiState = uiState, onSectionEvent = onSectionEvent)
    }

    if (uiState.deleteConfirmSectionId != null) {
        DeleteSectionDialog(onSectionEvent = onSectionEvent)
    }

    if (uiState.isEditingProject) {
        EditProjectDialog(uiState = uiState, onProjectEvent = onProjectEvent)
    }

    if (uiState.deleteConfirmProject) {
        DeleteProjectDialog(onProjectEvent = onProjectEvent)
    }
}
