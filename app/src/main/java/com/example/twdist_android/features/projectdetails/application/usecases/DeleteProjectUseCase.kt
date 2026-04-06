package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository

class DeleteProjectUseCase(
    private val repository: ProjectDetailsRepository
) {
    suspend operator fun invoke(projectId: Long): Result<Unit> {
        return repository.deleteProject(projectId)
    }
}

