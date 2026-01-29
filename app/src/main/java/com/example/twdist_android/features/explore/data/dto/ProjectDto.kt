package com.example.twdist_android.features.explore.data.dto

import kotlinx.serialization.Serializable

@Serializable
// To get data about a project
data class ProjectDto(
    val id: Long,
    val name: String
)