package com.example.twdist_android.features.projectdetails.domain.model

import java.time.LocalDate

data class Task(
    val id: Long,
    val sectionId: Long,
    val name: String,
    val completed: Boolean,
    val description: String? = null,
    /** When the task was completed (derived from backend `completedDate` when present). */
    val completedAt: LocalDate? = null,
    val startDate: String? = null,
    val endDate: String? = null
)
