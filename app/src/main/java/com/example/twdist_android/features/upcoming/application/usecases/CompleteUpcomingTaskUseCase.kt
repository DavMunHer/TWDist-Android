package com.example.twdist_android.features.upcoming.application.usecases

import com.example.twdist_android.features.upcoming.domain.repository.UpcomingRepository
import javax.inject.Inject

class CompleteUpcomingTaskUseCase @Inject constructor(
    private val repository: UpcomingRepository
) {
    suspend operator fun invoke(projectId: Long, sectionId: Long, taskId: Long): Result<Unit> =
        repository.completeTask(projectId, sectionId, taskId)
}
