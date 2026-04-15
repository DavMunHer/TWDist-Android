package com.example.twdist_android.features.projectdetails.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.features.projectdetails.data.mapper.toCompleteTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.dto.CreateTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.dto.UpdateTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.mapper.toDomainTask
import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import com.example.twdist_android.features.projectdetails.domain.store.TaskStateStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val api: ProjectDetailsApi,
    private val taskStateStore: TaskStateStore
) : TaskRepository {
    override suspend fun getTasksBySection(projectId: Long, sectionId: Long): Result<List<Task>> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.getTasksBySection(projectId, sectionId)
                if (!response.isSuccessful) {
                    error("Failed to fetch tasks (HTTP ${response.code()})")
                }
                val tasks = response.body()?.map { it.toDomainTask(sectionId) } ?: emptyList()
                taskStateStore.removeBySectionId(sectionId)
                taskStateStore.upsertAll(tasks)
                tasks
            }
        }
    }

    override suspend fun createTask(projectId: Long, sectionId: Long, name: TaskName): Result<Task> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.createTask(projectId, sectionId, CreateTaskRequestDto(name.asString()))
                if (!response.isSuccessful) {
                    error("Failed to create task (HTTP ${response.code()})")
                }
                val dto = response.body() ?: error("Task creation returned empty body")
                dto.toDomainTask(sectionId).also(taskStateStore::upsert)
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
                val response = api.updateTask(
                    projectId,
                    sectionId,
                    taskId,
                    UpdateTaskRequestDto(name = name.asString())
                )
                if (!response.isSuccessful) {
                    error("Failed to update task (HTTP ${response.code()})")
                }
                val dto = response.body() ?: error("Task update returned empty body")
                dto.toDomainTask(sectionId).also(taskStateStore::upsert)
            }
        }
    }

    override suspend fun completeTask(
        projectId: Long,
        sectionId: Long,
        taskId: Long,
        completedDate: LocalDate?
    ): Result<Task> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.completeTask(
                    projectId = projectId,
                    sectionId = sectionId,
                    taskId = taskId,
                    request = completedDate.toCompleteTaskRequestDto()
                )
                if (!response.isSuccessful) {
                    error("Failed to complete task (HTTP ${response.code()})")
                }
                val dto = response.body() ?: error("Task completion returned empty body")
                dto.toDomainTask(sectionId).also(taskStateStore::upsert)
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
                taskStateStore.remove(taskId)
            }
        }
    }
}
