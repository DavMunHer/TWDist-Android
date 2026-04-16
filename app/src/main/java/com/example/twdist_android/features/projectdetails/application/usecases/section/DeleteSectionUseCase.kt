package com.example.twdist_android.features.projectdetails.application.usecases.section

import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import javax.inject.Inject

class DeleteSectionUseCase @Inject constructor(
    private val repository: SectionRepository
) {
    suspend operator fun invoke(sectionId: Long): Result<Unit> {
        return repository.deleteSection(sectionId)
    }
}

