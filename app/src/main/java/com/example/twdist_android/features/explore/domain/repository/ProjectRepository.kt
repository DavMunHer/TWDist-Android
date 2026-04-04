package com.example.twdist_android.features.explore.domain.repository

import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.model.ProjectAggregate
import com.example.twdist_android.features.explore.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.model.ProjectSummary

interface ProjectRepository {
    suspend fun getAllProjects(): Result<List<ProjectSummary>>
    suspend fun getProjectById(projectId: Long): Result<ProjectAggregate>
    suspend fun createProject(projectName: ProjectName): Result<Project>
}
