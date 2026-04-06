package com.example.twdist_android.features.projectdetails.domain.repository

import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate

interface ProjectDetailsRepository {
    suspend fun getProjectById(projectId: Long): Result<ProjectAggregate>
    suspend fun updateProjectName(projectId: Long, name: String): Result<Unit>
    suspend fun deleteProject(projectId: Long): Result<Unit>
}
