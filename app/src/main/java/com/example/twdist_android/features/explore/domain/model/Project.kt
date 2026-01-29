package com.example.twdist_android.features.explore.domain.model

data class Project(
    val id: Long, // O String, según uses
    val name: String,
    val isFavorite: Boolean = false,
    // Placeholder for pending tasks count
    // TODO: Replace with actual data from backend
    val pendingTasks: Int = 0
)