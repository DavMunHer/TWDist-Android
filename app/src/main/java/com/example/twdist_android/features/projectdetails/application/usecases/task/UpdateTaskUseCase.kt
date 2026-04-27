package com.example.twdist_android.features.projectdetails.application.usecases.task

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
        name: TaskName,
        description: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Result<Task> {
        return repository.updateTask(
            projectId = projectId,
            sectionId = sectionId,
            taskId = taskId,
            name = name,
            description = description,
            startDate = startDate,
            endDate = endDate
        )
    }
}

