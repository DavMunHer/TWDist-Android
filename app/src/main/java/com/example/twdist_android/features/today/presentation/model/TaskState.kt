package com.example.twdist_android.features.today.presentation.model

data class TaskState(
    val id: Long,
    val projectId: Long,
    val sectionId: Long,
    val title: String,
    val projectName: String,
    val isCompleted: Boolean = false
)