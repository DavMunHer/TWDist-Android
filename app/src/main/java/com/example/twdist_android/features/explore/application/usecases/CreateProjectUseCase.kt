package com.example.twdist_android.features.explore.application.usecases

import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.domain.store.ProjectStateStore
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository,
    private val projectStateStore: ProjectStateStore
) {
    suspend operator fun invoke(projectName: ProjectName): Result<Project> {
        return repository.createProject(projectName)
            .onSuccess { project ->
                projectStateStore.upsert(
                    ProjectSummary(
                        id = project.id,
                        name = project.name,
                        isFavorite = project.isFavorite,
                        pendingTasks = 0
                    )
                )
            }
    }
}