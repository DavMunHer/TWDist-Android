package com.example.twdist_android.features.projectdetails.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState

@Composable
fun DeleteTaskDialog(
    uiState: ProjectDetailsUiState,
    onSectionEvent: (SectionEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onSectionEvent(SectionEvent.DeleteTaskDismissed) },
        title = { Text("Delete Task") },
        text = { Text("Are you sure you want to delete this task?") },
        confirmButton = {
            Button(
                onClick = { onSectionEvent(SectionEvent.DeleteTaskConfirmed) },
                enabled = !uiState.isTaskDeleteLoading
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = { onSectionEvent(SectionEvent.DeleteTaskDismissed) }) {
                Text("Cancel")
            }
        }
    )
}
