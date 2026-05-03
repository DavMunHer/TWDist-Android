package com.example.twdist_android.features.upcoming.domain.model

import java.time.LocalDate

data class UpcomingTask(
    val id: Long,
    val sectionId: Long,
    val projectId: Long,
    val name: String,
    val projectName: String,
    val endDate: LocalDate
)
