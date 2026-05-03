package com.example.twdist_android.features.upcoming.application.usecases

import com.example.twdist_android.features.upcoming.domain.repository.UpcomingRepository
import javax.inject.Inject

class UndoCompleteUpcomingTaskUseCase @Inject constructor(
    private val repository: UpcomingRepository
) {
    suspend operator fun invoke(projectId: Long, sectionId: Long, taskId: Long): Result<Unit> =
        repository.undoCompleteTask(projectId, sectionId, taskId)
}
