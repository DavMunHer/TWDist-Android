package com.example.twdist_android.features.explore.presentation.model

data class ProjectUi(
    val id: Long,
    val name: String,
    val isFavorite: Boolean,
    val pendingTasks: Int
)
