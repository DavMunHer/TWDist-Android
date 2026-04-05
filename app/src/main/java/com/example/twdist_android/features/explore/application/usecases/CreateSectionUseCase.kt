package com.example.twdist_android.features.explore.application.usecases

import com.example.twdist_android.features.explore.domain.model.Section
import com.example.twdist_android.features.explore.domain.model.SectionName
import com.example.twdist_android.features.explore.domain.repository.SectionRepository
import javax.inject.Inject

class CreateSectionUseCase @Inject constructor(
    private val repository: SectionRepository
) {
    suspend operator fun invoke(projectId: Long, sectionName: SectionName): Result<Section> {
        return repository.createSection(projectId, sectionName)
    }
}
