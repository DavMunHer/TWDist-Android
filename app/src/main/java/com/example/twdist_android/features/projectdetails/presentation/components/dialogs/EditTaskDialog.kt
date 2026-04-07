package com.example.twdist_android.features.projectdetails.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState

@Composable
fun EditTaskDialog(
    uiState: ProjectDetailsUiState,
    onSectionEvent: (SectionEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onSectionEvent(SectionEvent.EditTaskDismissed) },
        title = { Text("Edit Task") },
        text = {
            TextField(
                value = uiState.editingTaskName,
                onValueChange = { onSectionEvent(SectionEvent.EditTaskNameChanged(it)) },
                placeholder = { Text("Task name") },
                singleLine = true,
                isError = uiState.taskActionError != null,
                supportingText = uiState.taskActionError?.let {
                    { Text(text = it, color = MaterialTheme.colorScheme.error) }
                }
            )
        },
        confirmButton = {
            Button(
                onClick = { onSectionEvent(SectionEvent.EditTaskConfirmed) },
                enabled = !uiState.isTaskEditLoading
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onSectionEvent(SectionEvent.EditTaskDismissed) }) {
                Text("Cancel")
            }
        }
    )
}
