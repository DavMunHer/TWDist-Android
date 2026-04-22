package com.example.twdist_android.features.today.domain.model

data class TodayTask(
    val id: Long,
    val sectionId: Long,
    val name: String,
    val projectId: Long,
    val projectName: String
)
