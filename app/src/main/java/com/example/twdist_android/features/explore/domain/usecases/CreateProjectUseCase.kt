package com.example.twdist_android.features.explore.domain.usecases

import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.repository.ExploreRepository
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val repository: ExploreRepository
) {
    suspend operator fun invoke(name: String): Result<Unit> {
        return repository.createProject(name)
    }
}