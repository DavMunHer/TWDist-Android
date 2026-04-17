package com.example.twdist_android.features.projectdetails.data.dto.project

import com.example.twdist_android.core.network.FlexibleStringIdSerializer
import com.example.twdist_android.features.projectdetails.data.dto.section.SectionResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDetailResponseDto(
    @Serializable(with = FlexibleStringIdSerializer::class)
    val id: String,
    val name: String,
    val favorite: Boolean,
    val sections: List<SectionResponseDto> = emptyList()
)
