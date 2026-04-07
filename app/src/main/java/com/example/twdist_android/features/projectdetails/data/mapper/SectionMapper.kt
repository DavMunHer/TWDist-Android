package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.features.projectdetails.data.dto.SectionResponseDto
import com.example.twdist_android.features.projectdetails.data.dto.SectionUpdateResponseDto
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName

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

fun SectionUpdateResponseDto.toDomain(projectId: Long): Result<Section> {
    val sectionId = id.toLongOrNull()
        ?: return Result.failure(IllegalArgumentException("Invalid section id: $id"))

    val nameResult = SectionName.create(name)
    if (nameResult.isFailure) {
        return Result.failure(nameResult.exceptionOrNull()!!)
    }

    val taskIds = tasks.map { it.id.toString() }

    return Section.create(
        id = sectionId,
        projectId = projectId,
        name = nameResult.getOrThrow(),
        taskIds = taskIds
    )
}
