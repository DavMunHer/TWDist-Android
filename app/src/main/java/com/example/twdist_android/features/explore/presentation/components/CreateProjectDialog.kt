package com.example.twdist_android.features.explore.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun CreateProjectDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    error: String? = null
) {
    var projectName by remember() { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Project") },
        text = {
            TextField(
                value = projectName,
                onValueChange = { projectName = it },
                placeholder = { Text("Project name") },
                singleLine = true,
                isError = error != null,
                supportingText = error?.let {
                    {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(projectName)
                    // Don't dismiss automatically - let ViewModel handle success/error
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton (onClick = onDismiss) { Text("Cancel") }
        }
    )
}