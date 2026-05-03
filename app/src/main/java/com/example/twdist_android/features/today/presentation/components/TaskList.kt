package com.example.twdist_android.features.today.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.twdist_android.core.ui.components.task.TaskRow
import com.example.twdist_android.core.ui.components.task.TaskRowState

@Composable
fun TodayTaskList(
    tasks: List<TaskRowState>,
    onTaskCompleted: (TaskRowState) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks) { task ->
            TaskRow(
                task = task,
                onCompleted = onTaskCompleted
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskListPreview() {
    val sampleTasks = listOf(
        TaskRowState(
            id = 1L,
            projectId = 1L,
            sectionId = 1L,
            title = "Tema 1 y 2",
            projectName = "School"
        ),
        TaskRowState(
            id = 2L,
            projectId = 1L,
            sectionId = 1L,
            title = "Tareas de navidad",
            projectName = "Personal"
        ),
        TaskRowState(
            id = 3L,
            projectId = 1L,
            sectionId = 1L,
            title = "Repaso",
            projectName = "Work"
        ),
    )

    TodayTaskList(
        tasks = sampleTasks,
        onTaskCompleted = {}
    )
}
