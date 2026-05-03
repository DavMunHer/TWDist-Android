package com.example.twdist_android.features.upcoming.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

private val DAY_LABELS = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
fun WeeklyCalendar(
    weekStart: LocalDate,
    visibleDate: LocalDate,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()

    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "calendarArrowRotation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .animateContentSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Row 1: Month + year + arrow — entire row is the expand/collapse tap target
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggleExpanded)
                .padding(bottom = 8.dp)
        ) {
            val displayMonth = visibleDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
            val displayYear = visibleDate.year
            Text(
                text = "$displayMonth $displayYear",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = if (isExpanded) "Collapse calendar" else "Expand calendar",
                modifier = Modifier.rotate(arrowRotation)
            )
        }

        // Row 2: Day-of-week labels — always visible
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DAY_LABELS.forEach { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (!isExpanded) {
            // Collapsed: single week row
            CalendarWeekRow(
                weekStart = weekStart,
                visibleDate = visibleDate,
                today = today,
                currentMonth = null,
                onDayClick = onDayClick
            )
        } else {
            // Expanded: all weeks of the current month
            val yearMonth = YearMonth.from(visibleDate)
            val firstOfMonth = yearMonth.atDay(1)
            val lastOfMonth = yearMonth.atEndOfMonth()
            val gridStart = firstOfMonth.with(WeekFields.ISO.dayOfWeek(), 1)
            val gridEnd = lastOfMonth.with(WeekFields.ISO.dayOfWeek(), 7)

            var rowStart = gridStart
            while (!rowStart.isAfter(gridEnd)) {
                CalendarWeekRow(
                    weekStart = rowStart,
                    visibleDate = visibleDate,
                    today = today,
                    currentMonth = yearMonth.month,
                    onDayClick = onDayClick
                )
                rowStart = rowStart.plusWeeks(1)
            }
        }
    }
}

@Composable
private fun CalendarWeekRow(
    weekStart: LocalDate,
    visibleDate: LocalDate,
    today: LocalDate,
    currentMonth: Month?,
    onDayClick: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 0..6) {
            val day = weekStart.plusDays(i.toLong())
            val isHighlighted = day == visibleDate
            val isOutOfMonth = currentMonth != null && day.month != currentMonth
            val isPast = day.isBefore(today)
            val isTappable = !isPast && !isOutOfMonth

            // Outer Box: fixed height so rows never resize when the highlight circle
            // appears or disappears on this cell.
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
            ) {
                // Inner Box: always 32×32 and clipped to CircleShape so both the
                // background fill and the press ripple are circular.
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (isHighlighted && !isOutOfMonth) MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        )
                        .then(if (isTappable) Modifier.clickable { onDayClick(day) } else Modifier)
                ) {
                    Text(
                        text = day.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = when {
                            isHighlighted && !isOutOfMonth -> MaterialTheme.colorScheme.onPrimary
                            isOutOfMonth || isPast -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyCalendarCollapsedPreview() {
    val today = LocalDate.now()
    WeeklyCalendar(
        weekStart = today.with(WeekFields.ISO.dayOfWeek(), 1),
        visibleDate = today,
        isExpanded = false,
        onToggleExpanded = {},
        onDayClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun WeeklyCalendarExpandedPreview() {
    val today = LocalDate.now()
    WeeklyCalendar(
        weekStart = today.with(WeekFields.ISO.dayOfWeek(), 1),
        visibleDate = today,
        isExpanded = true,
        onToggleExpanded = {},
        onDayClick = {}
    )
}
