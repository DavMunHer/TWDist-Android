package com.example.twdist_android.features.projectdetails.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TaskResponseDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val completedDate: String? = null,
    val completed: Boolean = false,
    val subtasks: List<TaskResponseDto> = emptyList()
)
