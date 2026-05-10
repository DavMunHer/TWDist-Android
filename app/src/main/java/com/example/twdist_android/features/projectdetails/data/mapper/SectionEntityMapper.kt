package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.core.data.local.entity.SectionEntity
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName

fun Section.toEntity(): SectionEntity = SectionEntity(
    id = id,
    projectId = projectId,
    name = name.value
)

fun SectionEntity.toSection(taskIds: List<String> = emptyList()): Result<Section> {
    val nameResult = SectionName.create(name)
    if (nameResult.isFailure) return Result.failure(nameResult.exceptionOrNull()!!)
    return Section.create(
        id = id,
        projectId = projectId,
        name = nameResult.getOrThrow(),
        taskIds = taskIds
    )
}
