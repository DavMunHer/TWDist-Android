package com.example.twdist_android.features.projectdetails.domain.model

data class Task(
    val id: Long,
    val sectionId: Long,
    val name: String,
    val completed: Boolean
)
