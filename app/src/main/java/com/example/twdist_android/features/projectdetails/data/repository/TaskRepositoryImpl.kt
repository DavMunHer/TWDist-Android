package com.example.twdist_android.features.projectdetails.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.features.projectdetails.data.dto.CreateTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.dto.UpdateTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.mapper.toDomainTask
import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val api: ProjectDetailsApi
) : TaskRepository {
    override suspend fun getTasksBySection(projectId: Long, sectionId: Long): Result<List<Task>> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                api.getTasksBySection(projectId, sectionId).map { it.toDomainTask() }
            }
        }
    }

    override suspend fun createTask(projectId: Long, sectionId: Long, name: TaskName): Result<Task> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                api.createTask(projectId, sectionId, CreateTaskRequestDto(name.asString())).toDomainTask()
            }
        }
    }

    override suspend fun updateTask(
        projectId: Long,
        sectionId: Long,
        taskId: Long,
        name: TaskName
    ): Result<Task> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                api.updateTask(
                    projectId,
                    sectionId,
                    taskId,
                    UpdateTaskRequestDto(name.asString())
                ).toDomainTask()
            }
        }
    }

    override suspend fun deleteTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.deleteTask(projectId, sectionId, taskId)
                if (!response.isSuccessful) {
                    error("Failed to delete task (HTTP ${response.code()})")
                }
            }
        }
    }
}
