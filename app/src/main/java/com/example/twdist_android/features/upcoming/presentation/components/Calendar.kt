package com.example.twdist_android.features.upcoming.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.rememberCalendarState
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun Calendar(calendarState: CalendarState) {
    Column {
        MonthHeader(calendarState)
        CalendarContent(calendarState)
    }
}

@Preview
@Composable
fun CalendarPreview() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(0) }
    val endMonth = remember { currentMonth.plusMonths(0) }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.MONDAY
    )
    Calendar(calendarState)
}