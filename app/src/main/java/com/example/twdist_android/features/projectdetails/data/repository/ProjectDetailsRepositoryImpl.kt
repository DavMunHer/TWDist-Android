package com.example.twdist_android.features.projectdetails.data.repository

import android.util.Log
import com.example.twdist_android.BuildConfig
import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.explore.domain.store.ProjectStateStore
import com.example.twdist_android.features.projectdetails.data.dto.UpdateProjectRequestDto
import com.example.twdist_android.features.projectdetails.data.mapper.toDomainAggregate
import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ProjectDetailsRepositoryImpl @Inject constructor(
    private val api: ProjectDetailsApi,
    private val projectStateStore: ProjectStateStore
) : ProjectDetailsRepository {
    companion object {
        private const val TAG = "ProjectDetailsRepo"
    }

    override suspend fun getProjectById(projectId: Long): Result<ProjectAggregate> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                api.getProjectById(projectId).toDomainAggregate().getOrThrow()
            }
        }
    }

    override suspend fun updateProjectName(projectId: Long, name: String): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.updateProject(projectId, UpdateProjectRequestDto(name = name))
                if (!response.isSuccessful) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "updateProject failed. code=${response.code()}")
                    }
                    throw IllegalStateException("Could not update project")
                }
                val updatedName = ProjectName.create(name).getOrNull()
                if (updatedName != null) {
                    val cachedProject = projectStateStore.getById(projectId)
                    projectStateStore.upsert(
                        ProjectSummary(
                            id = projectId,
                            name = updatedName,
                            isFavorite = cachedProject?.isFavorite ?: response.body()?.favorite ?: false,
                            pendingTasks = cachedProject?.pendingTasks ?: 0
                        )
                    )
                }
                Unit
            }
        }.onFailure { throwable ->
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "updateProjectName threw exception", throwable)
            }
        }
    }

    override suspend fun deleteProject(projectId: Long): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.deleteProject(projectId)
                if (!response.isSuccessful) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "deleteProject failed. code=${response.code()}")
                    }
                    throw IllegalStateException("Could not delete project")
                }
                projectStateStore.remove(projectId)
                Unit
            }
        }
    }
}
