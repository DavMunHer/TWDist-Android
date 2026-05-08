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
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject

private fun parseAnyDate(raw: String?): LocalDate? {
    if (raw.isNullOrBlank()) return null
    return runCatching { LocalDate.parse(raw) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(raw).toLocalDate() }.getOrNull()
        ?: runCatching { Instant.parse(raw).atZone(ZoneId.systemDefault()).toLocalDate() }.getOrNull()
}

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
                val today = LocalDate.now()
                response.body().orEmpty()
                    .filter { dto ->
                        val taskStartDate = parseAnyDate(dto.startDate)
                        taskStartDate == today
                    }
                    .map { it.toDomainTodayTask() }
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
