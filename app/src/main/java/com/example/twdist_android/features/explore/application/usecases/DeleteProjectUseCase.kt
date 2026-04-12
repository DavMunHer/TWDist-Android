package com.example.twdist_android.features.explore.application.usecases

import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.domain.store.ProjectStateStore
import javax.inject.Inject

class DeleteProjectUseCase @Inject constructor(
    private val repository: ProjectRepository,
    private val projectStateStore: ProjectStateStore
) {
    suspend operator fun invoke(projectId: Long): Result<Unit> {
        return repository.deleteProject(projectId)
            .onSuccess { projectStateStore.remove(projectId) }
    }
}
