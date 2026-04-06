package com.example.twdist_android.features.projectdetails.domain.repository

import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName

interface SectionRepository {
    suspend fun getSectionsByProjectId(projectId: Long): Result<List<Section>>
    suspend fun createSection(projectId: Long, sectionName: SectionName): Result<Section>
    suspend fun updateSectionName(sectionId: Long, sectionName: SectionName): Result<Section>
    suspend fun deleteSection(sectionId: Long): Result<Unit>
    suspend fun addTaskIdToSection(sectionId: Long, taskId: String): Result<Section>
    suspend fun replaceTaskIdInSection(
        sectionId: Long,
        oldTaskId: String,
        newTaskId: String
    ): Result<Section>
    suspend fun removeTaskIdFromSection(sectionId: Long, taskId: String): Result<Section>
}
