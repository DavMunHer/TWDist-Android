package com.example.twdist_android.features.explore.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangeFavoriteRequestDto(
    val favorite: Boolean
)
