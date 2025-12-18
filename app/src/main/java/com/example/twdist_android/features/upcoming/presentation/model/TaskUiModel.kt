package com.example.twdist_android.features.upcoming.presentation.model

import java.time.LocalDate

data class TaskUiModel(
    val id: String,
    val name: String,
    val completed: Boolean,
    val start_date: LocalDate
)
