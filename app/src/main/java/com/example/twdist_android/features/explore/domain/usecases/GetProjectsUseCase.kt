package com.example.twdist_android.features.explore.domain.usecases

import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.domain.store.ProjectStateStore
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository,
    private val projectStateStore: ProjectStateStore
) {
    suspend operator fun invoke(): Result<List<ProjectSummary>> {
        return repository.getAllProjects()
            .onSuccess { projects -> projectStateStore.upsertAll(projects) }
    }
}