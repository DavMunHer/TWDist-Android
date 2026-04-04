package com.example.twdist_android.features.explore.domain.model

data class ProjectSummary(
    val id: Long,
    val name: ProjectName,
    val isFavorite: Boolean,
    val pendingTasks: Int
)
