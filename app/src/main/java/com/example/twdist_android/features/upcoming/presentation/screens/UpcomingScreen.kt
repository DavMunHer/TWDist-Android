package com.example.twdist_android.features.upcoming.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.upcoming.presentation.components.calendar.Calendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun UpcomingScreen() {
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

    }
}

@Preview
@Composable
fun UpcomingScreenPreview() {
    UpcomingScreen()
}