package com.example.twdist_android.core.data.local.projection

/**
 * JDBC/Room projection for Today/Upcoming JOIN queries.
 * [startEpochDay] is [java.time.LocalDate.toEpochDay] (INTEGER column) for deterministic date ordering/filtering.
 */
data class TaskWithProjectProjection(
    val taskId: Long,
    val taskName: String,
    val completed: Boolean,
    val startEpochDay: Long?,
    val sectionId: Long,
    val projectId: Long,
    val projectName: String
)
