package com.example.twdist_android.features.upcoming.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twdist_android.features.upcoming.presentation.components.UpcomingTaskList
import com.example.twdist_android.features.upcoming.presentation.components.WeeklyCalendar
import com.example.twdist_android.features.upcoming.presentation.model.UpcomingListItem
import com.example.twdist_android.features.upcoming.presentation.model.UpcomingUiEvent
import com.example.twdist_android.features.upcoming.presentation.viewmodel.UpcomingViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun UpcomingScreen(
    viewModel: UpcomingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var calendarExpanded by remember { mutableStateOf(false) }

    // Derive the date of the top-most visible item by walking back from firstVisibleItemIndex
    // to find the nearest Header or Task item.
    val topVisibleDate by remember(uiState.items) {
        derivedStateOf {
            val idx = listState.firstVisibleItemIndex
            uiState.items.take(idx + 1).asReversed().firstNotNullOfOrNull { item ->
                when (item) {
                    is UpcomingListItem.Header -> item.date
                    is UpcomingListItem.Task -> item.date
                }
            }
        }
    }

    LaunchedEffect(topVisibleDate) {
        topVisibleDate?.let { viewModel.onVisibleDateChanged(it) }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is UpcomingUiEvent.TaskCompleted -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Task completed",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.undoTaskCompleted(event.task, event.date)
                    }
                }
            }
        }
    }

    // Scroll the task list to the header matching the tapped day (past days are ignored by
    // the calendar — it won't call onDayClick for them).
    val onDayClick: (LocalDate) -> Unit = { date ->
        val idx = uiState.items.indexOfFirst {
            it is UpcomingListItem.Header && it.date == date
        }
        if (idx != -1) {
            coroutineScope.launch { listState.animateScrollToItem(idx) }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
            Text(
                text = "Upcoming",
                fontSize = 30.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            WeeklyCalendar(
                weekStart = uiState.weekStart,
                visibleDate = uiState.visibleDate,
                isExpanded = calendarExpanded,
                onToggleExpanded = { calendarExpanded = !calendarExpanded },
                onDayClick = onDayClick
            )
            HorizontalDivider()

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                UpcomingTaskList(
                    items = uiState.items,
                    listState = listState,
                    onTaskCompleted = viewModel::onTaskCompleted
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}
