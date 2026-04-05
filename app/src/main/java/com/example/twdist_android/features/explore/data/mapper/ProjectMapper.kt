package com.example.twdist_android.features.explore.data.mapper

import com.example.twdist_android.features.explore.data.dto.ProjectResponseDto
import com.example.twdist_android.features.explore.data.dto.ProjectSummaryDto
import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.model.ProjectSummary

fun ProjectSummaryDto.toDomainSummary(): Result<ProjectSummary> {
    val id = id.toLongOrNull()
        ?: return Result.failure(IllegalArgumentException("Invalid project id: $id"))

    val nameResult = ProjectName.create(name)
    if (nameResult.isFailure) {
        return Result.failure(nameResult.exceptionOrNull()!!)
    }

    return Result.success(
        ProjectSummary(
        id = id,
        name = nameResult.getOrThrow(),
        isFavorite = favorite,
        pendingTasks = pendingCount
        )
    )
}

fun ProjectResponseDto.toDomainResponse(): Result<Project> {
    val projectId = id.toLongOrNull()
        ?: return Result.failure(IllegalArgumentException("Invalid project id: $id"))

    val nameResult = ProjectName.create(name)
    if (nameResult.isFailure) {
        return Result.failure(nameResult.exceptionOrNull()!!)
    }

    val sectionIds = sections.orEmpty().mapNotNull { it.id.toLongOrNull() }

    return Project.create(
        id = projectId,
        name = nameResult.getOrThrow(),
        isFavorite = favorite,
        sectionIds = sectionIds
    )
}