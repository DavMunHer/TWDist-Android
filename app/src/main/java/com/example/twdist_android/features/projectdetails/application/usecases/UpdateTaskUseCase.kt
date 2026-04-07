package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        projectId: Long,
        sectionId: Long,
        taskId: Long,
        name: TaskName
    ): Result<Task> {
        return repository.updateTask(projectId, sectionId, taskId, name)
    }
}
