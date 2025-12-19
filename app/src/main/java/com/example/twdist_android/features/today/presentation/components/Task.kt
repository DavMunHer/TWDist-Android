package com.example.twdist_android.features.today.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twdist_android.features.favorite.presentation.components.FavoriteProjectCard
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectState
import com.example.twdist_android.features.today.presentation.model.TaskState

@Composable
fun Task(
    task: TaskState,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(border = BorderStroke(2.dp, Color.Black)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = task.title, fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskPreview() {
    var checked by remember { mutableStateOf(false) }

    Task(
        task = TaskState(
            title = "Observación de un ecosistema cercano",
            isCompleted = checked,
            id = 0
        ),
        onCheckedChange = { checked = it }
    )
}