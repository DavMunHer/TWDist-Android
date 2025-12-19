package com.example.twdist_android.features.today.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.twdist_android.core.ui.theme.TWDistAndroidTheme
import com.example.twdist_android.features.favorite.presentation.components.FavoriteProjectCard
import com.example.twdist_android.features.today.presentation.model.TaskState

@Composable
fun TaskList(
    tasks: List<TaskState>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks) { task ->
            Task(task = task, onCheckedChange = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskListPreview() {
    val sampleTasks = listOf(
        TaskState(
            id = 1,
            title = "Tema 1 y 2",
            isCompleted = false
        ),
        TaskState(
            id = 2,
            title = "Tareas de navidad",
            isCompleted = false
        ),
        TaskState(
            id = 3,
            title = "Repaso",
            isCompleted = false
        ),
    )

    TaskList(tasks = sampleTasks)
}
