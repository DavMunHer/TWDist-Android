package com.example.twdist_android.features.explore.application.usecases

import com.example.twdist_android.features.explore.domain.model.Section
import com.example.twdist_android.features.explore.domain.repository.SectionRepository
import javax.inject.Inject

class AddTaskIdToSectionUseCase @Inject constructor(
    private val repository: SectionRepository
) {
    suspend operator fun invoke(sectionId: Long, taskId: String): Result<Section> {
        return repository.addTaskIdToSection(sectionId, taskId)
    }
}
