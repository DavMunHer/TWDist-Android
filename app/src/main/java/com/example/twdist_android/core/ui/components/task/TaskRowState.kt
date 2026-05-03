package com.example.twdist_android.core.ui.components.task

data class TaskRowState(
    val id: Long,
    val projectId: Long,
    val sectionId: Long,
    val title: String,
    val projectName: String,
    val isCompleted: Boolean = false
)
