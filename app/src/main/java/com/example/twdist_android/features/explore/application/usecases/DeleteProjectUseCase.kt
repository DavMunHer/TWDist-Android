package com.example.twdist_android.features.explore.application.usecases

import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import javax.inject.Inject

class DeleteProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(projectId: Long): Result<Unit> =
        repository.deleteProject(projectId)
}
