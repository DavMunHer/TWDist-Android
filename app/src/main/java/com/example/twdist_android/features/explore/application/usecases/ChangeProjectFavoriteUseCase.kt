package com.example.twdist_android.features.explore.application.usecases

import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import javax.inject.Inject

class ChangeProjectFavoriteUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(projectId: Long, isFavorite: Boolean): Result<Unit> =
        repository.changeFavorite(projectId, isFavorite)
}
