package com.example.twdist_android.features.explore.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProjectDetailResponseDto(
    @Serializable(with = FlexibleStringIdSerializer::class)
    val id: String,
    val name: String,
    val favorite: Boolean,
    val sections: List<SectionResponseDto> = emptyList()
)
