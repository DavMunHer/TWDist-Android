package com.example.twdist_android.features.explore.data.store.model

data class SectionRecord(
    val id: Long,
    val projectId: Long,
    val name: String,
    val taskIds: List<String>
)
