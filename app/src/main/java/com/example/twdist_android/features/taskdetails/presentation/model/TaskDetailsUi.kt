package com.example.twdist_android.features.taskdetails.presentation.model

data class TaskDetailsUi(
    val id: Long,
    val name: String,
    val completed: Boolean,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null
)
