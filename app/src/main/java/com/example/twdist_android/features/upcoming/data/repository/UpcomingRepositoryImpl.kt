package com.example.twdist_android.features.upcoming.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.core.data.local.TWDistDatabase
import com.example.twdist_android.features.projectdetails.data.dto.task.CompleteTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.mapper.toCompleteTaskRequestDto
import com.example.twdist_android.features.upcoming.data.mapper.toDomainUpcomingTask
import com.example.twdist_android.features.upcoming.data.mapper.toProjectEntity
import com.example.twdist_android.features.upcoming.data.mapper.toSectionEntity
import com.example.twdist_android.features.upcoming.data.mapper.toTaskEntity
import com.example.twdist_android.features.upcoming.data.remote.UpcomingApi
import com.example.twdist_android.features.upcoming.domain.model.UpcomingTask
import com.example.twdist_android.features.upcoming.domain.repository.UpcomingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class UpcomingRepositoryImpl @Inject constructor(
    private val api: UpcomingApi,
    private val db: TWDistDatabase
) : UpcomingRepository {

    private val taskDao get() = db.taskDao()
    private val sectionDao get() = db.sectionDao()
    private val projectDao get() = db.projectDao()

    override fun observeUpcomingTasks(from: LocalDate, to: LocalDate): Flow<List<UpcomingTask>> =
        taskDao.observeByStartDateRange(from.toEpochDay(), to.toEpochDay())
            .map { rows -> rows.mapNotNull { it.toDomainUpcomingTask() } }

    override suspend fun refreshUpcomingTasks(from: LocalDate, to: LocalDate): Result<Unit> =
        runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.getUpcomingTasks(from = from.toString(), to = to.toString())
                if (!response.isSuccessful) error("Failed to fetch upcoming tasks (HTTP ${response.code()})")
                val dtos = response.body().orEmpty()
                dtos.forEach { dto ->
                    projectDao.insertIfMissing(dto.toProjectEntity())
                    sectionDao.insertIfMissing(dto.toSectionEntity())
                }
                taskDao.upsertAll(dtos.map { it.toTaskEntity() })
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
                if (!response.isSuccessful) error("Failed to complete task (HTTP ${response.code()})")
                val existing = taskDao.getById(taskId)
                if (existing != null) {
                    val completedDay = LocalDate.now()
                    taskDao.upsert(existing.copy(completed = true, completedDate = completedDay))
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
                if (!response.isSuccessful) error("Failed to undo task completion (HTTP ${response.code()})")
                val existing = taskDao.getById(taskId)
                if (existing != null) {
                    taskDao.upsert(existing.copy(completed = false, completedDate = null))
                }
            }
        }
    }
}
