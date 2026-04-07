package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksBySectionUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(projectId: Long, sectionId: Long): Result<List<Task>> {
        return repository.getTasksBySection(projectId, sectionId)
    }
}
