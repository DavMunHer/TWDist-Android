package com.example.twdist_android.features.explore.data.dto

import kotlinx.serialization.Serializable

@Serializable
// To send data
data class CreateProjectRequestDto(
    val name: String
)