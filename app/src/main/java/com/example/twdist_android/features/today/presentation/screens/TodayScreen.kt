package com.example.twdist_android.features.today.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twdist_android.features.today.presentation.components.TodayTaskList
import com.example.twdist_android.features.today.presentation.model.TaskState
import com.example.twdist_android.features.today.presentation.model.TodayUiEvent
import com.example.twdist_android.features.today.presentation.model.TodayUiState
import com.example.twdist_android.features.today.presentation.viewmodel.TodayViewModel

@Composable
fun TodayScreen(
    viewModel: TodayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is TodayUiEvent.TaskCompleted -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Task completed",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.undoTaskCompleted(event.task)
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TodayScreenContent(
            uiState = uiState,
            onTaskCompleted = viewModel::onTaskCompleted,
            modifier = Modifier
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

@Composable
fun TodayScreenContent(
    uiState: TodayUiState,
    onTaskCompleted: (TaskState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            text = uiState.title,
            fontSize = 30.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = uiState.formattedDate,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${uiState.totalCount} tasks",
                style = MaterialTheme.typography.titleSmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            TodayTaskList(
                tasks = uiState.tasks,
                onTaskCompleted = onTaskCompleted
            )
        }

        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodayScreenContentPreview() {
    TodayScreenContent(
        uiState = TodayUiState(
            formattedDate = "Monday, 22 Jan",
            tasks = listOf(
                TaskState(
                    id = 1L,
                    projectId = 1L,
                    sectionId = 1L,
                    title = "Estudiar tema 1",
                    projectName = "Ingles"
                ),
                TaskState(
                    id = 2L,
                    projectId = 1L,
                    sectionId = 1L,
                    title = "Ejercicio 2 y 3",
                    projectName = "Matematicas"
                )
            )
        ),
        onTaskCompleted = {}
    )
}