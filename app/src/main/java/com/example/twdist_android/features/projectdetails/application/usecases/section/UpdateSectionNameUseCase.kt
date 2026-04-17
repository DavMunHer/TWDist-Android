package com.example.twdist_android.features.projectdetails.application.usecases.section

import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import javax.inject.Inject

class UpdateSectionNameUseCase @Inject constructor(
    private val repository: SectionRepository
) {
    suspend operator fun invoke(sectionId: Long, sectionName: SectionName): Result<Section> {
        return repository.updateSectionName(sectionId, sectionName)
    }
}

