package com.example.twdist_android.features.upcoming.presentation.components.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun CalendarContent(state: CalendarState) {
    VerticalCalendar(
        state = state,
        dayContent = { day ->
            DayCell(day)
        }
    )
}

@Preview
@Composable
fun CalendarContentPreview() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(0) }
    val endMonth = remember { currentMonth.plusMonths(0) }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.MONDAY
    )
    CalendarContent(calendarState)
}
