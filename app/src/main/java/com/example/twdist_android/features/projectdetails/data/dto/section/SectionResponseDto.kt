package com.example.twdist_android.features.projectdetails.data.dto.section

import com.example.twdist_android.core.network.FlexibleStringIdSerializer
import kotlinx.serialization.Serializable

@Serializable
data class SectionResponseDto(
    @Serializable(with = FlexibleStringIdSerializer::class)
    val id: String,
    val name: String,
    val taskIds: List<String> = emptyList()
)
