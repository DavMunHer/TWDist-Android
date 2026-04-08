package com.example.twdist_android.features.projectdetails.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.projectdetails.presentation.event.TaskEvent

@Composable
internal fun AddTaskCard(
    sectionId: Long,
    onTaskEvent: (TaskEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskEvent(TaskEvent.AddTaskClicked(sectionId)) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Add task",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.surface
            )
        }
    }
}
