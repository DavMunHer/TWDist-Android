package com.example.twdist_android.features.upcoming.presentation.model

import com.example.twdist_android.core.ui.components.task.TaskRowState
import java.time.LocalDate

/** Days shown after [windowEnd] so the list can scroll the last selectable dates into reach. */
const val UPCOMING_SCROLL_PADDING_DAYS = 6

sealed interface UpcomingListItem {
    data class Header(val date: LocalDate) : UpcomingListItem
    data class Task(val state: TaskRowState, val date: LocalDate) : UpcomingListItem
    data class PaddingDay(val date: LocalDate) : UpcomingListItem
}
