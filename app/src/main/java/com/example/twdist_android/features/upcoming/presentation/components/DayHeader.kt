package com.example.twdist_android.features.upcoming.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val headerFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.ENGLISH)

@Composable
fun DayHeader(date: LocalDate, modifier: Modifier = Modifier) {
    val today = LocalDate.now()
    val suffix = when (date) {
        today -> " • Today"
        today.plusDays(1) -> " • Tomorrow"
        else -> ""
    }
    Text(
        text = date.format(headerFormatter) + suffix,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
