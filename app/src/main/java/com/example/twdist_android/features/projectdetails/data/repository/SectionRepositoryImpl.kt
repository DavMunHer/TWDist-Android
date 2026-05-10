package com.example.twdist_android.features.projectdetails.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.core.data.local.dao.SectionDao
import com.example.twdist_android.core.data.local.dao.TaskDao
import com.example.twdist_android.features.projectdetails.data.dto.section.UpdateSectionRequestDto
import com.example.twdist_android.features.projectdetails.data.mapper.toDomain
import com.example.twdist_android.features.projectdetails.data.mapper.toDomainAggregate
import com.example.twdist_android.features.projectdetails.data.mapper.toEntity
import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SectionRepositoryImpl @Inject constructor(
    private val api: ProjectDetailsApi,
    private val sectionDao: SectionDao,
    private val taskDao: TaskDao
) : SectionRepository {

    override suspend fun getSectionsByProjectId(projectId: Long): Result<List<Section>> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val sections = api.getProjectById(projectId)
                    .toDomainAggregate()
                    .getOrThrow()
                    .sections
                sectionDao.upsertAll(sections.map { it.toEntity() })
                sections
            }
        }
    }

    override suspend fun createSection(projectId: Long, sectionName: SectionName): Result<Section> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val dto = api.createSection(
                    projectId = projectId,
                    request = UpdateSectionRequestDto(name = sectionName.asString())
                )
                val createdSection = dto.toDomain(projectId).getOrThrow()
                sectionDao.upsert(createdSection.toEntity())
                createdSection
            }
        }
    }

    override suspend fun updateSectionName(sectionId: Long, sectionName: SectionName): Result<Section> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val currentEntity = sectionDao.getById(sectionId)
                    ?: error("Section not found in local cache")

                val dto = api.updateSection(
                    projectId = currentEntity.projectId,
                    sectionId = sectionId,
                    request = UpdateSectionRequestDto(name = sectionName.asString())
                )
                val updatedSection = dto.toDomain(currentEntity.projectId).getOrThrow()
                sectionDao.upsert(updatedSection.toEntity())
                updatedSection
            }
        }
    }

    override suspend fun deleteSection(sectionId: Long): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val currentEntity = sectionDao.getById(sectionId)
                    ?: error("Section not found in local cache")
                val response = api.deleteSection(
                    projectId = currentEntity.projectId,
                    sectionId = sectionId
                )
                if (!response.isSuccessful) {
                    error("Failed to delete section (HTTP ${response.code()})")
                }
                taskDao.deleteBySectionId(sectionId)
                sectionDao.deleteById(sectionId)
            }
        }
    }

    override suspend fun addTaskIdToSection(sectionId: Long, taskId: String): Result<Section> {
        return Result.failure(
            NotImplementedError("Section task-id endpoint is not implemented in ProjectDetailsApi yet")
        )
    }

    override suspend fun replaceTaskIdInSection(
        sectionId: Long,
        oldTaskId: String,
        newTaskId: String
    ): Result<Section> {
        return Result.failure(
            NotImplementedError("Section task-id replace endpoint is not implemented in ProjectDetailsApi yet")
        )
    }

    override suspend fun removeTaskIdFromSection(sectionId: Long, taskId: String): Result<Section> {
        return Result.failure(
            NotImplementedError("Section task-id removal endpoint is not implemented in ProjectDetailsApi yet")
        )
    }
}
