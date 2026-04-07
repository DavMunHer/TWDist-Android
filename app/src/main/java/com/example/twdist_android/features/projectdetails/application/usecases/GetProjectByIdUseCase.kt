package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.store.ProjectDetailsProjectStateStore
import com.example.twdist_android.features.projectdetails.domain.store.SectionStateStore
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import javax.inject.Inject

class GetProjectByIdUseCase @Inject constructor(
    private val repository: ProjectDetailsRepository,
    private val projectStateStore: ProjectDetailsProjectStateStore,
    private val sectionStateStore: SectionStateStore
) {
    suspend operator fun invoke(projectId: Long): Result<ProjectAggregate> {
        return repository.getProjectById(projectId)
            .onSuccess { aggregate ->
                projectStateStore.upsert(aggregate.project)
                sectionStateStore.upsertAll(aggregate.sections)
            }
    }
}
