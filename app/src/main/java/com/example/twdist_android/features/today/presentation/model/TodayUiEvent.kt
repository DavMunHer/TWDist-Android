package com.example.twdist_android.features.today.presentation.model

import com.example.twdist_android.core.ui.components.task.TaskRowState

sealed interface TodayUiEvent {
    data class TaskCompleted(val task: TaskRowState) : TodayUiEvent
}
