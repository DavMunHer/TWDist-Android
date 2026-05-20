package com.example.twdist_android.features.upcoming.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twdist_android.core.ui.components.ScreenHeader
import com.example.twdist_android.core.ui.components.task.TaskRowState
import com.example.twdist_android.features.auth.presentation.viewmodel.SessionViewModel
import com.example.twdist_android.core.ui.theme.TWDistAndroidTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twdist_android.features.upcoming.presentation.components.UpcomingTaskList
import com.example.twdist_android.features.upcoming.presentation.components.WeeklyCalendar
import com.example.twdist_android.features.upcoming.presentation.model.UpcomingListItem
import com.example.twdist_android.features.upcoming.presentation.model.UpcomingUiState
import com.example.twdist_android.features.upcoming.presentation.model.UpcomingUiEvent
import com.example.twdist_android.features.upcoming.presentation.viewmodel.UpcomingViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun UpcomingScreen(
    viewModel: UpcomingViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var calendarExpanded by remember { mutableStateOf(false) }

    // Silently refresh the task list every time this screen resumes so that tasks
    // whose startDate was changed from another screen appear in the correct position
    // without needing an app restart.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshTasks(showLoading = false)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

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

    UpcomingScreenContent(
        uiState = uiState,
        listState = listState,
        calendarExpanded = calendarExpanded,
        onToggleCalendarExpanded = { calendarExpanded = !calendarExpanded },
        onDayClick = onDayClick,
        onTaskCompleted = viewModel::onTaskCompleted,
        onLogout = { sessionViewModel.logout() },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun UpcomingScreenContent(
    uiState: UpcomingUiState,
    listState: LazyListState,
    calendarExpanded: Boolean,
    onToggleCalendarExpanded: () -> Unit,
    onDayClick: (LocalDate) -> Unit,
    onTaskCompleted: (TaskRowState) -> Unit,
    onLogout: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
            ScreenHeader(
                title = "Upcoming",
                onLogout = onLogout
            )
            Spacer(modifier = Modifier.height(16.dp))
            WeeklyCalendar(
                weekStart = uiState.weekStart,
                visibleDate = uiState.visibleDate,
                isExpanded = calendarExpanded,
                onToggleExpanded = onToggleCalendarExpanded,
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UpcomingScreenContentPreview() {
    val today = LocalDate.now()
    TWDistAndroidTheme {
        UpcomingScreenContent(
            uiState = UpcomingUiState(
                items = listOf(
                    UpcomingListItem.Header(today),
                    UpcomingListItem.Task(
                        state = TaskRowState(
                            id = 1L,
                            projectId = 1L,
                            sectionId = 1L,
                            title = "Review notes",
                            projectName = "University"
                        ),
                        date = today
                    )
                ),
                visibleDate = today,
                weekStart = today
            ),
            listState = rememberLazyListState(),
            calendarExpanded = false,
            onToggleCalendarExpanded = {},
            onDayClick = {},
            onTaskCompleted = {},
            onLogout = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UpcomingScreenLoadingPreview() {
    TWDistAndroidTheme {
        UpcomingScreenContent(
            uiState = UpcomingUiState(isLoading = true),
            listState = rememberLazyListState(),
            calendarExpanded = false,
            onToggleCalendarExpanded = {},
            onDayClick = {},
            onTaskCompleted = {},
            onLogout = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
