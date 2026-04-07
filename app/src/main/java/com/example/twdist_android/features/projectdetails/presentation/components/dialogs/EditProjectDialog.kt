package com.example.twdist_android.features.projectdetails.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.example.twdist_android.features.projectdetails.presentation.event.ProjectEvent
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState

@Composable
fun EditProjectDialog(
    uiState: ProjectDetailsUiState,
    onProjectEvent: (ProjectEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onProjectEvent(ProjectEvent.EditDismissed) },
        title = { Text("Edit Project") },
        text = {
            TextField(
                value = uiState.editingProjectName,
                onValueChange = { onProjectEvent(ProjectEvent.NameChanged(it)) },
                placeholder = { Text("Project name") },
                singleLine = true,
                isError = uiState.projectActionError != null,
                supportingText = uiState.projectActionError?.let {
                    { Text(text = it, color = MaterialTheme.colorScheme.error) }
                }
            )
        },
        confirmButton = {
            Button(onClick = { onProjectEvent(ProjectEvent.EditConfirmed) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onProjectEvent(ProjectEvent.EditDismissed) }) {
                Text("Cancel")
            }
        }
    )
}
