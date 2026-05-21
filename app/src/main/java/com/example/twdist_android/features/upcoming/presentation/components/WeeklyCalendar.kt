package com.example.twdist_android.features.upcoming.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.twdist_android.features.upcoming.presentation.model.UPCOMING_SCROLL_PADDING_DAYS
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

private val DAY_LABELS = listOf("M", "T", "W", "T", "F", "S", "S")

private fun Modifier.monthChangeSeparator(
    color: Color,
    placement: MonthSeparatorPlacement
): Modifier = drawBehind {
    val stroke = 1.dp.toPx()
    when (placement) {
        MonthSeparatorPlacement.Above -> drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = stroke
        )
        is MonthSeparatorPlacement.WithinWeek -> {
            val cellWidth = size.width / 7f
            val boundaryX = cellWidth * placement.boundaryIndex
            val segmentStart = boundaryX - cellWidth * 0.15f
            val segmentEnd = boundaryX + cellWidth * 0.15f
            val y = size.height - stroke / 2f
            drawLine(
                color = color,
                start = Offset(segmentStart.coerceAtLeast(0f), y),
                end = Offset(segmentEnd.coerceAtMost(size.width), y),
                strokeWidth = stroke
            )
        }
    }
}

private sealed interface MonthSeparatorPlacement {
    data object Above : MonthSeparatorPlacement
    data class WithinWeek(val boundaryIndex: Int) : MonthSeparatorPlacement
}

private fun monthBoundaryIndexInWeek(weekStart: LocalDate): Int? =
    (0..5).firstOrNull { dayIndex ->
        val day = weekStart.plusDays(dayIndex.toLong())
        day.plusDays(1).month != day.month
    }?.let { it + 1 }

@Composable
fun WeeklyCalendar(
    weekStart: LocalDate,
    visibleDate: LocalDate,
    windowStart: LocalDate,
    windowEnd: LocalDate,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val monthSeparatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)

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
                windowStart = windowStart,
                windowEnd = windowEnd,
                monthSeparatorColor = monthSeparatorColor,
                onDayClick = onDayClick
            )
        } else {
            // Expanded: weeks through window end plus scroll padding (non-selectable tail days).
            val gridStart = windowStart.with(WeekFields.ISO.dayOfWeek(), 1)
            val scrollPaddingEnd = windowEnd.plusDays(UPCOMING_SCROLL_PADDING_DAYS.toLong())
            val gridEnd = scrollPaddingEnd.with(WeekFields.ISO.dayOfWeek(), 7)
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 280.dp)
                    .verticalScroll(scrollState)
            ) {
                var rowStart = gridStart
                var previousWeekStart: LocalDate? = null
                while (!rowStart.isAfter(gridEnd)) {
                    val showSeparatorAbove = previousWeekStart != null &&
                        previousWeekStart.plusDays(6).month != rowStart.month
                    val withinWeekBoundary = monthBoundaryIndexInWeek(rowStart)
                    CalendarWeekRow(
                        weekStart = rowStart,
                        visibleDate = visibleDate,
                        today = today,
                        windowStart = windowStart,
                        windowEnd = windowEnd,
                        monthSeparatorColor = monthSeparatorColor,
                        separatorAbove = showSeparatorAbove,
                        withinWeekBoundaryIndex = withinWeekBoundary,
                        onDayClick = onDayClick
                    )
                    previousWeekStart = rowStart
                    rowStart = rowStart.plusWeeks(1)
                }
            }
        }
    }
}

@Composable
private fun CalendarWeekRow(
    weekStart: LocalDate,
    visibleDate: LocalDate,
    today: LocalDate,
    windowStart: LocalDate,
    windowEnd: LocalDate,
    monthSeparatorColor: Color,
    separatorAbove: Boolean = false,
    withinWeekBoundaryIndex: Int? = null,
    onDayClick: (LocalDate) -> Unit
) {
    val rowSeparatorModifier = when {
        separatorAbove -> Modifier.monthChangeSeparator(monthSeparatorColor, MonthSeparatorPlacement.Above)
        withinWeekBoundaryIndex != null -> Modifier.monthChangeSeparator(
            monthSeparatorColor,
            MonthSeparatorPlacement.WithinWeek(withinWeekBoundaryIndex)
        )
        else -> Modifier
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(rowSeparatorModifier),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 0..6) {
            val day = weekStart.plusDays(i.toLong())
            val isHighlighted = day == visibleDate
            val isOutOfWindow = day.isBefore(windowStart) || day.isAfter(windowEnd)
            val isPast = day.isBefore(today)
            val isTappable = !isPast && !isOutOfWindow

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
                            if (isHighlighted && !isOutOfWindow) MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        )
                        .then(if (isTappable) Modifier.clickable { onDayClick(day) } else Modifier)
                ) {
                    Text(
                        text = day.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = when {
                            isHighlighted && !isOutOfWindow -> MaterialTheme.colorScheme.onPrimary
                            isOutOfWindow || isPast -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
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
        windowStart = today,
        windowEnd = today.plusMonths(1),
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
        windowStart = today,
        windowEnd = today.plusMonths(1),
        isExpanded = true,
        onToggleExpanded = {},
        onDayClick = {}
    )
}
