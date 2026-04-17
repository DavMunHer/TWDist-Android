package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.data.dto.project.ProjectDetailResponseDto

fun ProjectDetailResponseDto.toDomainAggregate(): Result<ProjectAggregate> {
    val projectId = id.toLongOrNull()
        ?: return Result.failure(IllegalArgumentException("Invalid project id: $id"))

    val mappedSections = sections.map { it.toDomain(projectId) }
    val sectionFailure = mappedSections.firstOrNull { it.isFailure }?.exceptionOrNull()
    if (sectionFailure != null) {
        return Result.failure(sectionFailure)
    }

    val sectionAggregates = mappedSections.map { it.getOrThrow() }
    val nameResult = ProjectName.create(name)
    if (nameResult.isFailure) {
        return Result.failure(nameResult.exceptionOrNull()!!)
    }

    val project = Project.create(
        id = projectId,
        name = nameResult.getOrThrow(),
        isFavorite = favorite,
        sectionIds = sectionAggregates.map { it.id }
    )
    if (project.isFailure) {
        return Result.failure(project.exceptionOrNull()!!)
    }

    return Result.success(ProjectAggregate(project.getOrThrow(), sectionAggregates))
}
