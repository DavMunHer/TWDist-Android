package com.example.twdist_android.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.twdist_android.core.data.local.entity.ProjectEntity

@Dao
interface ProjectDao {

    @Upsert
    suspend fun upsert(entity: ProjectEntity)

    @Upsert
    suspend fun upsertAll(entities: List<ProjectEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfMissing(entity: ProjectEntity)

    @Query("SELECT * FROM project WHERE id = :id")
    suspend fun getById(id: Long): ProjectEntity?

    @Query("SELECT * FROM project")
    suspend fun getAll(): List<ProjectEntity>

    @Query("DELETE FROM project WHERE id = :id")
    suspend fun deleteById(id: Long)

    /**
     * Open (incomplete) tasks for the project — suitable for deriving pending counts instead of caching them on [ProjectEntity].
     */
    @Query(
        """
        SELECT COUNT(*) FROM task AS t
        INNER JOIN section AS s ON s.id = t.section_id
        WHERE s.project_id = :projectId AND t.completed = 0
        """
    )
    suspend fun countIncompleteTasks(projectId: Long): Int
}
