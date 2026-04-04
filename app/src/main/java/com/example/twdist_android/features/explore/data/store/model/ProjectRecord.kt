package com.example.twdist_android.features.explore.data.store.model

data class ProjectRecord(
    val id: Long,
    val name: String,
    val isFavorite: Boolean,
    val pendingTasks: Int
)
