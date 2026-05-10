package com.example.twdist_android.features.projectdetails.application.usecases.project

import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import javax.inject.Inject

class GetProjectByIdUseCase @Inject constructor(
    private val repository: ProjectDetailsRepository
) {
    suspend operator fun invoke(projectId: Long): Result<ProjectAggregate> =
        repository.getProjectById(projectId)
}
