package com.example.twdist_android.features.projectdetails.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent

@Composable
fun DeleteSectionDialog(
    onSectionEvent: (SectionEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onSectionEvent(SectionEvent.DeleteDismissed) },
        title = { Text("Delete Section") },
        text = { Text("Are you sure you want to delete this section?") },
        confirmButton = {
            Button(onClick = { onSectionEvent(SectionEvent.DeleteConfirmed) }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = { onSectionEvent(SectionEvent.DeleteDismissed) }) {
                Text("Cancel")
            }
        }
    )
}
