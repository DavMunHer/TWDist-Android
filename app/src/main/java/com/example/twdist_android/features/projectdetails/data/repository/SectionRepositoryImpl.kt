package com.example.twdist_android.features.projectdetails.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.features.projectdetails.data.mapper.toDomainAggregate
import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SectionRepositoryImpl @Inject constructor(
    private val api: ProjectDetailsApi
) : SectionRepository {

    override suspend fun getSectionsByProjectId(projectId: Long): Result<List<Section>> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                api.getProjectById(projectId)
                    .toDomainAggregate()
                    .getOrThrow()
                    .sections
            }
        }
    }

    override suspend fun createSection(projectId: Long, sectionName: SectionName): Result<Section> {
        return Result.failure(
            NotImplementedError("Section creation endpoint is not implemented in ProjectDetailsApi yet")
        )
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
