package com.example.twdist_android.features.projectdetails.data.repository

import android.util.Log
import com.example.twdist_android.BuildConfig
import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.core.data.local.TWDistDatabase
import com.example.twdist_android.features.projectdetails.data.dto.project.UpdateProjectRequestDto
import com.example.twdist_android.features.projectdetails.data.mapper.toDomainAggregate
import com.example.twdist_android.features.projectdetails.data.mapper.toEntity
import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProjectDetailsRepositoryImpl @Inject constructor(
    private val api: ProjectDetailsApi,
    private val db: TWDistDatabase
) : ProjectDetailsRepository {

    private val projectDao get() = db.projectDao()
    private val sectionDao get() = db.sectionDao()
    companion object {
        private const val TAG = "ProjectDetailsRepo"
    }

    override suspend fun getProjectById(projectId: Long): Result<ProjectAggregate> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val aggregate = api.getProjectById(projectId).toDomainAggregate().getOrThrow()
                projectDao.upsert(aggregate.project.toEntity())
                sectionDao.upsertAll(aggregate.sections.map { it.toEntity() })
                aggregate
            }
        }
    }

    override suspend fun updateProjectName(projectId: Long, name: String): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.updateProject(projectId, UpdateProjectRequestDto(name = name))
                if (!response.isSuccessful) {
                    if (BuildConfig.DEBUG) Log.e(TAG, "updateProject failed. code=${response.code()}")
                    throw IllegalStateException("Could not update project")
                }
                val updatedName = ProjectName.create(name).getOrNull()
                if (updatedName != null) {
                    val existing = projectDao.getById(projectId)
                    if (existing != null) {
                        projectDao.upsert(existing.copy(name = updatedName.value))
                    }
                }
                Unit
            }
        }.onFailure { throwable ->
            if (BuildConfig.DEBUG) Log.e(TAG, "updateProjectName threw exception", throwable)
        }
    }

    override suspend fun deleteProject(projectId: Long): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.deleteProject(projectId)
                if (!response.isSuccessful) {
                    if (BuildConfig.DEBUG) Log.e(TAG, "deleteProject failed. code=${response.code()}")
                    throw IllegalStateException("Could not delete project")
                }
                projectDao.deleteById(projectId)
                Unit
            }
        }
    }
}
