package com.example.twdist_android.features.explore.domain.usecases

import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.repository.ExploreRepository

class CreateProjectUseCase(private val repository: ExploreRepository) {
    suspend operator fun invoke(project: Project): Result<Unit> {
        return repository.createProject(project)
    }
}