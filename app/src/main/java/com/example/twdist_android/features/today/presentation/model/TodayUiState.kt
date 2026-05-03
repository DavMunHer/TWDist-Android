package com.example.twdist_android.features.today.presentation.model

import com.example.twdist_android.core.ui.components.task.TaskRowState

data class TodayUiState(
    val isLoading: Boolean = false,
    val title: String = "Today",
    val formattedDate: String = "",
    val tasks: List<TaskRowState> = emptyList(),
    val error: String? = null
) {
    val totalCount: Int
        get() = tasks.size
}
