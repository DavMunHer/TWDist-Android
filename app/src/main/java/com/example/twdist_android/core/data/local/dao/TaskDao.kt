package com.example.twdist_android.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.twdist_android.core.data.local.entity.TaskEntity
import com.example.twdist_android.core.data.local.projection.TaskWithProjectProjection
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsert(entity: TaskEntity)

    @Upsert
    suspend fun upsertAll(entities: List<TaskEntity>)

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getById(id: Long): TaskEntity?

    @Query("SELECT * FROM task WHERE section_id = :sectionId")
    suspend fun getBySectionId(sectionId: Long): List<TaskEntity>

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM task WHERE section_id = :sectionId")
    suspend fun deleteBySectionId(sectionId: Long)

    @Query("""
        SELECT t.id AS taskId, t.name AS taskName, t.completed,
               t.start_date AS startEpochDay, t.section_id AS sectionId,
               s.project_id AS projectId, p.name AS projectName
        FROM task t
        INNER JOIN section s ON s.id = t.section_id
        INNER JOIN project p ON p.id = s.project_id
        WHERE t.start_date = :startEpochDay AND t.completed = 0
        ORDER BY p.name, t.id
    """)
    fun observeByStartDate(startEpochDay: Long): Flow<List<TaskWithProjectProjection>>

    @Query("""
        SELECT t.id AS taskId, t.name AS taskName, t.completed,
               t.start_date AS startEpochDay, t.section_id AS sectionId,
               s.project_id AS projectId, p.name AS projectName
        FROM task t
        INNER JOIN section s ON s.id = t.section_id
        INNER JOIN project p ON p.id = s.project_id
        WHERE t.start_date IS NOT NULL
          AND t.start_date BETWEEN :fromEpochDay AND :toEpochDay
          AND t.completed = 0
        ORDER BY t.start_date, t.id
    """)
    fun observeByStartDateRange(fromEpochDay: Long, toEpochDay: Long): Flow<List<TaskWithProjectProjection>>
}
