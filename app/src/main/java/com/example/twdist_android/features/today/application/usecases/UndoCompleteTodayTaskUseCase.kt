package com.example.twdist_android.features.today.application.usecases

import com.example.twdist_android.features.today.domain.repository.TodayRepository
import javax.inject.Inject

class UndoCompleteTodayTaskUseCase @Inject constructor(
    private val repository: TodayRepository
) {
    suspend operator fun invoke(projectId: Long, sectionId: Long, taskId: Long): Result<Unit> =
        repository.undoCompleteTask(projectId, sectionId, taskId)
}
