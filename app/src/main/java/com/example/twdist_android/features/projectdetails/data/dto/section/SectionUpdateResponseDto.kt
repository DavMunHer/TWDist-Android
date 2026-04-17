package com.example.twdist_android.features.projectdetails.data.dto.section

import com.example.twdist_android.core.network.FlexibleStringIdSerializer
import com.example.twdist_android.features.projectdetails.data.dto.task.TaskResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class SectionUpdateResponseDto(
    @Serializable(with = FlexibleStringIdSerializer::class)
    val id: String,
    val name: String,
    val tasks: List<TaskResponseDto>? = emptyList()
)
