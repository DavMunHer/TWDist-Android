package com.example.twdist_android.features.explore.domain.repository

import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.model.ProjectName

interface ProjectRepository {
    suspend fun getAllProjects(): Result<List<Project>>
    suspend fun createProject(projectName: ProjectName): Result<Project>
}
