package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository

class UpdateProjectNameUseCase(
    private val repository: ProjectDetailsRepository
) {
    suspend operator fun invoke(projectId: Long, name: ProjectName): Result<Unit> {
        return repository.updateProjectName(projectId, name.asString())
    }
}

