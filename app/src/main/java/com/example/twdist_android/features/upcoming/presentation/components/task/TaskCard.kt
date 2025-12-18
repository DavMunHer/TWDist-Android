package com.example.twdist_android.features.upcoming.presentation.components.task

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.twdist_android.features.upcoming.presentation.model.TaskUiModel
import java.time.LocalDate

@Composable
fun TaskCard(taskInfo: TaskUiModel, onTaskChange: (Boolean) -> Unit) {
    Row {
        Checkbox(
            checked = taskInfo.completed,
            onCheckedChange = { onTaskChange(it) }
        )
        Text(taskInfo.name)
    }
}

@Preview
@Composable
fun TaskCardPreview() {
    val taskInfo: TaskUiModel = TaskUiModel(
        "1", "My task 1", true, LocalDate.now()
    )
    TaskCard(taskInfo, {})
}