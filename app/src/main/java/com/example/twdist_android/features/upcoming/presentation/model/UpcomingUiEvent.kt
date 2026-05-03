package com.example.twdist_android.features.upcoming.presentation.model

import com.example.twdist_android.core.ui.components.task.TaskRowState
import java.time.LocalDate

sealed interface UpcomingUiEvent {
    data class TaskCompleted(val task: TaskRowState, val date: LocalDate) : UpcomingUiEvent
}
