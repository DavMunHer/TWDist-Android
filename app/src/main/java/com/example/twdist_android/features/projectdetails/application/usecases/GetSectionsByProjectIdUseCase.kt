package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import com.example.twdist_android.features.projectdetails.domain.store.SectionStateStore
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
