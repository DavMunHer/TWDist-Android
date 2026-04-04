package com.example.twdist_android.features.explore.domain.usecases

import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.domain.store.SectionStateStore
import javax.inject.Inject

class GetProjectByIdUseCase @Inject constructor(
    private val repository: ProjectRepository,
    private val sectionStateStore: SectionStateStore
) {
    suspend operator fun invoke(projectId: Long): Result<Project> {
        return repository.getProjectById(projectId)
            .onSuccess { aggregate ->
                sectionStateStore.upsertAll(aggregate.sections)
            }
            .map { aggregate -> aggregate.project }
    }
}
