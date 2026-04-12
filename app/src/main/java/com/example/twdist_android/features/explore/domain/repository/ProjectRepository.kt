package com.example.twdist_android.features.explore.domain.repository

import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.model.ProjectSummary

interface ProjectRepository {
    suspend fun getAllProjects(): Result<List<ProjectSummary>>
    suspend fun createProject(projectName: ProjectName): Result<Project>
    suspend fun deleteProject(projectId: Long): Result<Unit>
}
