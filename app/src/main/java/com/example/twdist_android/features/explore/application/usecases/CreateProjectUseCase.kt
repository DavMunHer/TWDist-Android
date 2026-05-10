package com.example.twdist_android.features.explore.application.usecases

import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(projectName: ProjectName): Result<Project> =
        repository.createProject(projectName)
}
