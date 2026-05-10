package com.example.twdist_android.features.explore.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.core.data.local.dao.ProjectDao
import com.example.twdist_android.features.explore.data.dto.ChangeFavoriteRequestDto
import com.example.twdist_android.features.explore.data.dto.CreateProjectRequestDto
import com.example.twdist_android.features.explore.data.mapper.toDomainResponse
import com.example.twdist_android.features.explore.data.mapper.toDomainSummary
import com.example.twdist_android.features.explore.data.mapper.toEntity
import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val api: ExploreApi,
    private val projectDao: ProjectDao
) : ProjectRepository {

    override suspend fun getAllProjects(): Result<List<ProjectSummary>> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val mappedProjects = api.getProjects().map { it.toDomainSummary() }
                val failure = mappedProjects.firstOrNull { it.isFailure }?.exceptionOrNull()
                if (failure != null) throw failure
                val projects = mappedProjects.map { it.getOrThrow() }
                projectDao.upsertAll(projects.map { it.toEntity() })
                projects
            }
        }
    }

    override suspend fun createProject(projectName: ProjectName): Result<Project> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val request = CreateProjectRequestDto(name = projectName.asString())
                val project = api.createProject(request).toDomainResponse().getOrThrow()
                projectDao.upsert(
                    com.example.twdist_android.core.data.local.entity.ProjectEntity(
                        id = project.id,
                        name = project.name.value,
                        isFavorite = project.isFavorite,
                        pendingTasks = 0
                    )
                )
                project
            }
        }
    }

    override suspend fun deleteProject(projectId: Long): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.deleteProject(projectId)
                if (!response.isSuccessful) throw HttpException(response)
                projectDao.deleteById(projectId)
            }
        }
    }

    override suspend fun changeFavorite(projectId: Long, isFavorite: Boolean): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.changeFavorite(
                    projectId = projectId,
                    request = ChangeFavoriteRequestDto(favorite = isFavorite)
                )
                if (!response.isSuccessful) throw HttpException(response)
                val existing = projectDao.getById(projectId)
                if (existing != null) {
                    projectDao.upsert(existing.copy(isFavorite = isFavorite))
                }
            }
        }
    }
}
