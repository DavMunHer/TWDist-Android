package com.example.twdist_android.features.explore.domain.model

import com.example.twdist_android.features.projectdetails.domain.model.ProjectName

data class ProjectSummary(
    val id: Long,
    val name: ProjectName,
    val isFavorite: Boolean,
    val pendingTasks: Int
)
