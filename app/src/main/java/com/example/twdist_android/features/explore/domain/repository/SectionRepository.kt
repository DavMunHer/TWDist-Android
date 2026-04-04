package com.example.twdist_android.features.explore.domain.repository

import com.example.twdist_android.features.explore.domain.model.Section
import com.example.twdist_android.features.explore.domain.model.SectionName

interface SectionRepository {
    suspend fun getSectionById(projectId: Long, sectionId: Long): Result<Section>
    suspend fun getSectionsByProjectId(projectId: Long): Result<List<Section>>
    suspend fun createSection(projectId: Long, sectionName: SectionName): Result<Section>
    suspend fun addTaskIdToSection(sectionId: Long, taskId: String): Result<Section>
    suspend fun replaceTaskIdInSection(
        sectionId: Long,
        oldTaskId: String,
        newTaskId: String
    ): Result<Section>
    suspend fun removeTaskIdFromSection(sectionId: Long, taskId: String): Result<Section>
}
