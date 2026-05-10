package com.example.twdist_android.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.twdist_android.core.data.local.entity.SectionEntity

@Dao
interface SectionDao {

    @Upsert
    suspend fun upsert(entity: SectionEntity)

    @Upsert
    suspend fun upsertAll(entities: List<SectionEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfMissing(entity: SectionEntity)

    @Query("SELECT * FROM section WHERE id = :id")
    suspend fun getById(id: Long): SectionEntity?

    @Query("SELECT * FROM section WHERE project_id = :projectId")
    suspend fun getByProjectId(projectId: Long): List<SectionEntity>

    @Query("SELECT id FROM section WHERE project_id = :projectId")
    suspend fun getIdsByProjectId(projectId: Long): List<Long>

    @Query("DELETE FROM section WHERE id = :id")
    suspend fun deleteById(id: Long)
}
