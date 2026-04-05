package com.example.twdist_android.features.projectdetails.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.features.projectdetails.data.mapper.toDomainAggregate
import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.explore.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ProjectDetailsRepositoryImpl @Inject constructor(
    private val api: ProjectDetailsApi
) : ProjectDetailsRepository {

    override suspend fun getProjectById(projectId: Long): Result<ProjectAggregate> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                api.getProjectById(projectId).toDomainAggregate().getOrThrow()
            }
        }
    }
}
