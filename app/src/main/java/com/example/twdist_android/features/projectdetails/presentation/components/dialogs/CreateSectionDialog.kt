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
fun CreateSectionDialog(
    uiState: ProjectDetailsUiState,
    onSectionEvent: (SectionEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onSectionEvent(SectionEvent.CreateSectionDismissed) },
        title = { Text("New Section") },
        text = {
            TextField(
                value = uiState.creatingSectionName,
                onValueChange = { onSectionEvent(SectionEvent.CreateSectionNameChanged(it)) },
                placeholder = { Text("Section name") },
                singleLine = true,
                isError = uiState.sectionActionError != null,
                supportingText = uiState.sectionActionError?.let {
                    { Text(text = it, color = MaterialTheme.colorScheme.error) }
                }
            )
        },
        confirmButton = {
            Button(
                onClick = { onSectionEvent(SectionEvent.CreateSectionConfirmed) },
                enabled = !uiState.isSectionCreateLoading
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onSectionEvent(SectionEvent.CreateSectionDismissed) }) {
                Text("Cancel")
            }
        }
    )
}
