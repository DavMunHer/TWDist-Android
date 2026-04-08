package com.example.twdist_android.features.projectdetails.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.example.twdist_android.features.projectdetails.presentation.event.TaskEvent
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState

@Composable
fun CreateTaskDialog(
    uiState: ProjectDetailsUiState,
    onTaskEvent: (TaskEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onTaskEvent(TaskEvent.CreateTaskDismissed) },
        title = { Text("Add Task") },
        text = {
            TextField(
                value = uiState.creatingTaskName,
                onValueChange = { onTaskEvent(TaskEvent.CreateTaskNameChanged(it)) },
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
                onClick = { onTaskEvent(TaskEvent.CreateTaskConfirmed) },
                enabled = !uiState.isTaskCreateLoading
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onTaskEvent(TaskEvent.CreateTaskDismissed) }) {
                Text("Cancel")
            }
        }
    )
}
