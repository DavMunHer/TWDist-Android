package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.explore.domain.model.ProjectAggregate
import com.example.twdist_android.features.explore.domain.store.SectionStateStore
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import javax.inject.Inject

class GetProjectByIdUseCase @Inject constructor(
    private val repository: ProjectDetailsRepository,
    private val sectionStateStore: SectionStateStore
) {
    suspend operator fun invoke(projectId: Long): Result<ProjectAggregate> {
        return repository.getProjectById(projectId)
            .onSuccess { aggregate ->
                sectionStateStore.upsertAll(aggregate.sections)
            }
    }
}
