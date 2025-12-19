package com.example.twdist_android.features.today.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twdist_android.features.today.presentation.components.TaskList
import com.example.twdist_android.features.today.presentation.model.TaskState

@Composable
fun TodayScreen(modifier: Modifier = Modifier, tasks: List<TaskState>) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Ingles",
            fontSize = 30.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        TaskList(
            tasks = tasks,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TodayScreenPreview() {
    val tasks = listOf(
        TaskState(
            id = 1,
            title = "Estudiar tema 1",
            isCompleted = true
        ), TaskState(
            id = 2,
            title = "Ejercicio 2 y 3",
            isCompleted = true
        )
    )
    TodayScreen(tasks = tasks)
}