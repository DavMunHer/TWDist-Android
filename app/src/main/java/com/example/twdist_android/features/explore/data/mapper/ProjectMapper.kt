package com.example.twdist_android.features.explore.data.mapper

import com.example.twdist_android.features.explore.data.dto.ProjectDetailResponseDto
import com.example.twdist_android.features.explore.data.dto.ProjectResponseDto
import com.example.twdist_android.features.explore.data.dto.ProjectSummaryDto
import com.example.twdist_android.features.explore.data.dto.SectionResponseDto
import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.model.ProjectAggregate
import com.example.twdist_android.features.explore.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.explore.domain.model.Section
import com.example.twdist_android.features.explore.domain.model.SectionName

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

    val sectionIds = sections.orEmpty().mapNotNull { section -> section.id.toLongOrNull() }

    return Project.create(
        id = projectId,
        name = nameResult.getOrThrow(),
        isFavorite = favorite,
        sectionIds = sectionIds
    )
}

fun SectionResponseDto.toDomain(projectId: Long): Result<Section> {
    val sectionId = id.toLongOrNull()
        ?: return Result.failure(IllegalArgumentException("Invalid section id: $id"))

    val nameResult = SectionName.create(name)
    if (nameResult.isFailure) {
        return Result.failure(nameResult.exceptionOrNull()!!)
    }

    return Section.create(
        id = sectionId,
        projectId = projectId,
        name = nameResult.getOrThrow(),
        taskIds = taskIds
    )
}

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