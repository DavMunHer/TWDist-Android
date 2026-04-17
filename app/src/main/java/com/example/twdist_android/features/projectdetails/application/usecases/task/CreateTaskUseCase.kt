package com.example.twdist_android.features.projectdetails.application.usecases.task

import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(projectId: Long, sectionId: Long, name: TaskName): Result<Task> {
        return repository.createTask(projectId, sectionId, name)
    }
}

