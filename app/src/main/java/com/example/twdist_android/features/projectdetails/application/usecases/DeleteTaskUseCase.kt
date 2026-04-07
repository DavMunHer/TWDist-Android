package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(projectId: Long, sectionId: Long, taskId: Long): Result<Unit> {
        return repository.deleteTask(projectId, sectionId, taskId)
    }
}
