package com.example.twdist_android.features.upcoming.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.upcoming.presentation.components.calendar.Calendar
import com.example.twdist_android.features.upcoming.presentation.components.task.TaskCard
import com.example.twdist_android.features.upcoming.presentation.model.TaskUiModel
import com.example.twdist_android.features.upcoming.presentation.model.UpcomingTasksUiState
import com.kizitonwose.calendar.compose.rememberCalendarState
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun UpcomingScreen(uiState: MutableStateFlow<UpcomingTasksUiState>) { // The uiState will have to be changed by the viewModel
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(0) }
    val endMonth = remember { currentMonth.plusMonths(0) }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.MONDAY
    )


    Column {
        Calendar(calendarState)
        HorizontalDivider(
            modifier = Modifier.padding(30.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )

        LazyColumn {
            items(uiState.value.tasksList) { item ->
                TaskCard(item, {})
            }
        }
    }
}

@Preview
@Composable
fun UpcomingScreenPreview() {
    val upcomingTasksUiState = MutableStateFlow<UpcomingTasksUiState>(
        UpcomingTasksUiState(
            isLoading = false,
            tasksList = listOf(
                TaskUiModel(
                    "1",
                    "Task 1",
                    true,
                    LocalDate.now()
                ),
                TaskUiModel(
                    "2",
                    "Task 2",
                    false,
                    LocalDate.of(2025, 12, 22)
                )
            )
        )
    )
    UpcomingScreen(upcomingTasksUiState)
}