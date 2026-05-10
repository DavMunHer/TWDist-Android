package com.example.twdist_android.features.projectdetails.application.usecases.section

import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import javax.inject.Inject

class GetSectionsByProjectIdUseCase @Inject constructor(
    private val repository: SectionRepository
) {
    suspend operator fun invoke(projectId: Long): Result<List<Section>> =
        repository.getSectionsByProjectId(projectId)
}
