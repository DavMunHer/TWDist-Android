package com.example.twdist_android.features.upcoming.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpcomingTaskResponseDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String,
    val sectionId: Long,
    val projectId: Long,
    val projectName: String
)
