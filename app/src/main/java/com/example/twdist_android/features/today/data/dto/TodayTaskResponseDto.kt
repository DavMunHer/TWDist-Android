package com.example.twdist_android.features.today.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodayTaskResponseDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val completedDate: String? = null,
    val sectionId: Long,
    val projectId: Long,
    val projectName: String
)
