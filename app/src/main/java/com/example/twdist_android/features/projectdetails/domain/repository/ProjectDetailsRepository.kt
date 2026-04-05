package com.example.twdist_android.features.projectdetails.domain.repository

import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate

interface ProjectDetailsRepository {
    suspend fun getProjectById(projectId: Long): Result<ProjectAggregate>
}
