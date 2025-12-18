package com.example.twdist_android.features.upcoming.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate

@Composable
fun DayCell(day: CalendarDay) {

    val isToday = day.date == LocalDate.now()
    val isCurrentMonth = day.position == DayPosition.MonthDate

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clickable(enabled = isCurrentMonth) {
                println("Día seleccionado: ${day.date}")
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = when {
                !isCurrentMonth -> Color.LightGray
                isToday -> Color.Blue
                else -> Color.Black
            }
        )
    }
}

@Preview
@Composable
fun DayCellPreview() {
    val previewDay = CalendarDay(
        date = LocalDate.now(),
        position = DayPosition.MonthDate,
    )

    DayCell(day = previewDay)
}
