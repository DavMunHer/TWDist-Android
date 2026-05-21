package com.example.twdist_android.features.upcoming.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val headerFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.ENGLISH)

@Composable
fun DayHeader(
    date: LocalDate,
    showMonthSeparator: Boolean = false,
    isPadding: Boolean = false,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val suffix = if (isPadding) {
        ""
    } else {
        when (date) {
            today -> " • Today"
            today.plusDays(1) -> " • Tomorrow"
            else -> ""
        }
    }
    val monthSeparatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    val textColor = if (isPadding) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    Text(
        text = date.format(headerFormatter) + suffix,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = textColor,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(
                if (showMonthSeparator) {
                    Modifier.drawBehind {
                        val stroke = 1.dp.toPx()
                        drawLine(
                            color = monthSeparatorColor,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = stroke
                        )
                    }
                } else {
                    Modifier
                }
            )
    )
}
