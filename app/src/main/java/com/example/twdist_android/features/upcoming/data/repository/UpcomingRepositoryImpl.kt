package com.example.twdist_android.features.upcoming.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.features.projectdetails.data.dto.task.CompleteTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.mapper.toCompleteTaskRequestDto
import com.example.twdist_android.features.upcoming.data.mapper.toDomainUpcomingTask
import com.example.twdist_android.features.upcoming.data.remote.UpcomingApi
import com.example.twdist_android.features.upcoming.domain.model.UpcomingTask
import com.example.twdist_android.features.upcoming.domain.repository.UpcomingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class UpcomingRepositoryImpl @Inject constructor(
    private val api: UpcomingApi
) : UpcomingRepository {

    override suspend fun getUpcomingTasks(from: LocalDate, to: LocalDate): Result<List<UpcomingTask>> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.getUpcomingTasks(
                    from = from.toString(),
                    to = to.toString()
                )
                if (!response.isSuccessful) {
                    error("Failed to fetch upcoming tasks (HTTP ${response.code()})")
                }
                response.body().orEmpty().map { it.toDomainUpcomingTask() }
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
