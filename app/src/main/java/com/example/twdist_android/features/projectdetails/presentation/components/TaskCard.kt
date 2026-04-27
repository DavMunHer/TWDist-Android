package com.example.twdist_android.features.projectdetails.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.projectdetails.presentation.event.TaskEvent
import com.example.twdist_android.features.projectdetails.presentation.model.TaskUi

@Composable
fun TaskCard(
    sectionId: Long,
    taskItem: TaskUi,
    isTaskMenuOpen: Boolean,
    onTaskEvent: (TaskEvent) -> Unit,
    onTaskClick: ((sectionId: Long, taskId: Long) -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onTaskClick != null) {
                onTaskClick?.invoke(sectionId, taskItem.id)
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
            Icon(
                imageVector = if (taskItem.completed) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                contentDescription = "Task completion",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onTaskEvent(
                            TaskEvent.TaskCompletionToggled(
                                sectionId = sectionId,
                                taskId = taskItem.id
                            )
                        )
                    },
                tint = if (taskItem.completed) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = taskItem.name,
                style = MaterialTheme.typography.bodyLarge
            )
            }
            Box {
                IconButton(onClick = { onTaskEvent(TaskEvent.TaskMenuOpened(taskItem.id)) }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Task options"
                    )
                }
                DropdownMenu(
                    expanded = isTaskMenuOpen,
                    onDismissRequest = { onTaskEvent(TaskEvent.TaskMenuDismissed) }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Edit") },
                        onClick = {
                            onTaskEvent(
                                TaskEvent.EditTaskClicked(
                                    sectionId = sectionId,
                                    taskId = taskItem.id
                                )
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Delete") },
                        onClick = {
                            onTaskEvent(
                                TaskEvent.DeleteTaskClicked(
                                    sectionId = sectionId,
                                    taskId = taskItem.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
