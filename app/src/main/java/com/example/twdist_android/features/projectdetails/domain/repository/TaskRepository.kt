package com.example.twdist_android.features.projectdetails.domain.repository

import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import java.time.LocalDate

interface TaskRepository {
    suspend fun getTasksBySection(projectId: Long, sectionId: Long): Result<List<Task>>
    suspend fun createTask(projectId: Long, sectionId: Long, name: TaskName): Result<Task>
    suspend fun updateTask(
        projectId: Long,
        sectionId: Long,
        taskId: Long,
        name: TaskName,
        description: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Result<Task>
    suspend fun completeTask(projectId: Long, sectionId: Long, taskId: Long, completedDate: LocalDate?): Result<Task>
    suspend fun deleteTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit>
}
