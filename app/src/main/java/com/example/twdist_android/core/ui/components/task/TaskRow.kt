package com.example.twdist_android.core.ui.components.task

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TaskRow(
    task: TaskRowState,
    onCompleted: (TaskRowState) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = task.isCompleted,
                onClick = { onCompleted(task) }
            )
            Column {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = task.projectName,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskRowPreview() {
    TaskRow(
        task = TaskRowState(
            id = 0L,
            projectId = 1L,
            sectionId = 1L,
            title = "Write API integration tests",
            projectName = "Backend"
        ),
        onCompleted = {}
    )
}
