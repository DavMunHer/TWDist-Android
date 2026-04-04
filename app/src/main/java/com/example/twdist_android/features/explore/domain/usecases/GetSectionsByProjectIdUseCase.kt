package com.example.twdist_android.features.explore.domain.usecases

import com.example.twdist_android.features.explore.domain.model.Section
import com.example.twdist_android.features.explore.domain.repository.SectionRepository
import com.example.twdist_android.features.explore.domain.store.SectionStateStore
import javax.inject.Inject

class GetSectionsByProjectIdUseCase @Inject constructor(
    private val repository: SectionRepository,
    private val sectionStateStore: SectionStateStore
) {
    suspend operator fun invoke(projectId: Long): Result<List<Section>> {
        return repository.getSectionsByProjectId(projectId)
            .onSuccess { sections -> sectionStateStore.upsertAll(sections) }
    }
}
