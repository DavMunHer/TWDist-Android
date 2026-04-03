package com.example.twdist_android.features.explore.domain.usecases

import com.example.twdist_android.features.explore.domain.model.Section
import com.example.twdist_android.features.explore.domain.repository.SectionRepository
import javax.inject.Inject

class GetSectionsByProjectIdUseCase @Inject constructor(
    private val repository: SectionRepository
) {
    suspend operator fun invoke(projectId: Long): Result<List<Section>> {
        return repository.getSectionsByProjectId(projectId)
    }
}
