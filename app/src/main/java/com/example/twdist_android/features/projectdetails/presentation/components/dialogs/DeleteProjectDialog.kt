package com.example.twdist_android.features.projectdetails.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.twdist_android.features.projectdetails.presentation.event.ProjectEvent

@Composable
fun DeleteProjectDialog(
    onProjectEvent: (ProjectEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onProjectEvent(ProjectEvent.DeleteDismissed) },
        title = { Text("Delete Project") },
        text = { Text("Are you sure you want to delete this project?") },
        confirmButton = {
            Button(onClick = { onProjectEvent(ProjectEvent.DeleteConfirmed) }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = { onProjectEvent(ProjectEvent.DeleteDismissed) }) {
                Text("Cancel")
            }
        }
    )
}
