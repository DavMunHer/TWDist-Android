package com.example.twdist_android.core.data.local.projection

data class TaskWithProjectProjection(
    val taskId: Long,
    val taskName: String,
    val completed: Boolean,
    val startDate: String?,
    val sectionId: Long,
    val projectId: Long,
    val projectName: String
)
