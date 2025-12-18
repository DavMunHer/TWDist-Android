package com.example.twdist_android.features.upcoming.presentation.model

data class UpcomingTasksUiState(
    val isLoading: Boolean = false,
    val tasksList: List<TaskUiModel> = emptyList(),
    val error: String? = null
)