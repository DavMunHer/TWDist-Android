package com.example.twdist_android.features.explore.domain.model

data class Project(
    val id: Long,
    val name: String,
    val isFavorite: Boolean = false,
    val pendingTasks: Int = 0
)