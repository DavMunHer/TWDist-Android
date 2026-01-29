package com.example.twdist_android.features.explore.domain.usecases

import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.repository.ExploreRepository
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(
    private val repository: ExploreRepository
) {
    suspend operator fun invoke(): Result<List<Project>> {
        // Here you can filter, sort, or transform data if necessary
        return repository.getAllProjects()
    }
}