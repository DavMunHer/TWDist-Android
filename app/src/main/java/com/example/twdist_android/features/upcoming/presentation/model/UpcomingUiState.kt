package com.example.twdist_android.features.upcoming.presentation.model

import java.time.LocalDate
import java.time.temporal.WeekFields

data class UpcomingUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<UpcomingListItem> = emptyList(),
    val visibleDate: LocalDate = LocalDate.now(),
    val weekStart: LocalDate = LocalDate.now().with(WeekFields.ISO.dayOfWeek(), 1)
)
