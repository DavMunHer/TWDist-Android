package com.example.twdist_android.features.projectdetails.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompleteTaskRequestDto(
    val completedDate: String? = null
)
