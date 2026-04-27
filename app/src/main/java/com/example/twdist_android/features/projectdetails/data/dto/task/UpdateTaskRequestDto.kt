package com.example.twdist_android.features.projectdetails.data.dto.task

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTaskRequestDto(
    val name: String,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null
)
