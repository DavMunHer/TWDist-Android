package com.example.twdist_android.features.explore.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SectionResponseDto(
    @Serializable(with = FlexibleStringIdSerializer::class)
    val id: String,
    val name: String,
    val taskIds: List<String> = emptyList()
)
