package com.example.twdist_android.features.explore.application.usecases

import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.domain.store.ProjectStateStore
import javax.inject.Inject

class ChangeProjectFavoriteUseCase @Inject constructor(
    private val repository: ProjectRepository,
    private val projectStateStore: ProjectStateStore
) {
    suspend operator fun invoke(projectId: Long, isFavorite: Boolean): Result<Unit> {
        return repository.changeFavorite(projectId, isFavorite)
            .onSuccess {
                val existing = projectStateStore.getById(projectId)
                if (existing != null) {
                    projectStateStore.upsert(existing.copy(isFavorite = isFavorite))
                }
            }
    }
}
