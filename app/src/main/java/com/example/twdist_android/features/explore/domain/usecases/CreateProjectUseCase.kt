package com.example.twdist_android.features.explore.domain.usecases

import com.example.twdist_android.features.explore.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.repository.ExploreRepository
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val repository: ExploreRepository
) {
    suspend operator fun invoke(projectName: ProjectName): Result<Unit> {
        return repository.createProject(projectName)
    }
}