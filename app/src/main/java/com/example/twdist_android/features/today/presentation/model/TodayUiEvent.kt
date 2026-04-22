package com.example.twdist_android.features.today.presentation.model

sealed interface TodayUiEvent {
    data class TaskCompleted(val task: TaskState) : TodayUiEvent
}
