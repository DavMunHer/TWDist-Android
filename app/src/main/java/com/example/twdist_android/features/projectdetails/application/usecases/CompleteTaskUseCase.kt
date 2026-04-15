package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import java.time.LocalDate
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        projectId: Long,
        sectionId: Long,
        taskId: Long,
        completedDate: LocalDate?
    ): Result<Task> {
        return repository.completeTask(projectId, sectionId, taskId, completedDate)
    }
}
