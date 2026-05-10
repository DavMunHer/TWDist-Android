package com.example.twdist_android.features.today.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.core.data.local.TWDistDatabase
import com.example.twdist_android.features.projectdetails.data.dto.task.CompleteTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.mapper.toCompleteTaskRequestDto
import com.example.twdist_android.features.today.data.mapper.toDomainTodayTask
import com.example.twdist_android.features.today.data.mapper.toProjectEntity
import com.example.twdist_android.features.today.data.mapper.toSectionEntity
import com.example.twdist_android.features.today.data.mapper.toTaskEntity
import com.example.twdist_android.features.today.data.remote.TodayApi
import com.example.twdist_android.features.today.domain.model.TodayTask
import com.example.twdist_android.features.today.domain.repository.TodayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class TodayRepositoryImpl @Inject constructor(
    private val api: TodayApi,
    private val db: TWDistDatabase
) : TodayRepository {

    private val taskDao get() = db.taskDao()
    private val sectionDao get() = db.sectionDao()
    private val projectDao get() = db.projectDao()

    override fun observeTodayTasks(today: LocalDate): Flow<List<TodayTask>> =
        taskDao.observeByStartDate(today.toEpochDay())
            .map { rows -> rows.map { it.toDomainTodayTask() } }

    override suspend fun refreshTodayTasks(): Result<Unit> = runSuspendCatching {
        withContext(Dispatchers.IO) {
            val response = api.getTodayTasks()
            if (!response.isSuccessful) error("Failed to fetch today tasks (HTTP ${response.code()})")
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
                    taskDao.upsert(
                        existing.copy(completed = true, completedDate = completedDay)
                    )
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
                    taskDao.upsert(
                        existing.copy(completed = false, completedDate = null)
                    )
                }
            }
        }
    }
}
