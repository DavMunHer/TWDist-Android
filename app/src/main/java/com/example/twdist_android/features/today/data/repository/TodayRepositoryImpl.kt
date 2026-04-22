package com.example.twdist_android.features.today.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.features.projectdetails.data.dto.task.CompleteTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.mapper.toCompleteTaskRequestDto
import com.example.twdist_android.features.today.data.mapper.toDomainTodayTask
import com.example.twdist_android.features.today.data.remote.TodayApi
import com.example.twdist_android.features.today.domain.model.TodayTask
import com.example.twdist_android.features.today.domain.repository.TodayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class TodayRepositoryImpl @Inject constructor(
    private val api: TodayApi
) : TodayRepository {

    override suspend fun getTodayTasks(): Result<List<TodayTask>> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.getTodayTasks()
                if (!response.isSuccessful) {
                    error("Failed to fetch today tasks (HTTP ${response.code()})")
                }
                response.body().orEmpty().map { it.toDomainTodayTask() }
            }
        }
    }

    override suspend fun completeTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.completeTask(
                    projectId = projectId,
                    sectionId = sectionId,
                    taskId = taskId,
                    request = LocalDate.now().toCompleteTaskRequestDto()
                )
                if (!response.isSuccessful) {
                    error("Failed to complete task (HTTP ${response.code()})")
                }
            }
        }
    }

    override suspend fun undoCompleteTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.completeTask(
                    projectId = projectId,
                    sectionId = sectionId,
                    taskId = taskId,
                    request = CompleteTaskRequestDto(completedDate = null)
                )
                if (!response.isSuccessful) {
                    error("Failed to undo task completion (HTTP ${response.code()})")
                }
            }
        }
    }
}
