package com.example.twdist_android.features.upcoming.presentation.model

import com.example.twdist_android.core.ui.components.task.TaskRowState
import java.time.LocalDate

sealed interface UpcomingListItem {
    data class Header(val date: LocalDate) : UpcomingListItem
    data class Task(val state: TaskRowState, val date: LocalDate) : UpcomingListItem
}
