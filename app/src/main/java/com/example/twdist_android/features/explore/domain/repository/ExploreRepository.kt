package com.example.twdist_android.features.explore.domain.repository

import com.example.twdist_android.features.explore.domain.model.Project

interface ExploreRepository {
    suspend fun getAllProjects(): Result<List<Project>>
    suspend fun createProject(project: Project): Result<Unit>
}