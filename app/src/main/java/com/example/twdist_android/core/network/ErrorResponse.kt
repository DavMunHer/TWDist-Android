package com.example.twdist_android.core.network

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String
)
