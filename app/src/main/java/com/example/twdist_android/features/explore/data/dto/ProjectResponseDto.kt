package com.example.twdist_android.features.explore.data.dto

import com.example.twdist_android.core.network.FlexibleStringIdSerializer
import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponseDto(
    @Serializable(with = FlexibleStringIdSerializer::class)
    val id: String,
    val name: String,
    val favorite: Boolean,
    val sections: List<SectionResponseDto>? = null
)
