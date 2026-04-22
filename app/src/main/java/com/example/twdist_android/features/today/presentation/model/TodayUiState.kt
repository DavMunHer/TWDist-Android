package com.example.twdist_android.features.today.presentation.model

data class TodayUiState(
    val isLoading: Boolean = false,
    val title: String = "Today",
    val formattedDate: String = "",
    val tasks: List<TaskState> = emptyList(),
    val error: String? = null
) {
    val totalCount: Int
        get() = tasks.size
}
