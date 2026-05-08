package com.example.twdist_android.core.events

import java.time.LocalDate

data class TaskStartDateUpdatedEvent(
    val taskId: Long,
    val projectId: Long,
    val sectionId: Long,
    val taskName: String,
    val projectName: String,
    val newStartDate: LocalDate?
)
