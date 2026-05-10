package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.core.data.local.date.parseFlexibleToLocalDateOrNull
import com.example.twdist_android.core.data.local.entity.TaskEntity
import com.example.twdist_android.features.projectdetails.domain.model.Task

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    sectionId = sectionId,
    name = name,
    completed = completed,
    completedDate = completedAt,
    description = description,
    startDate = startDate?.parseFlexibleToLocalDateOrNull(),
    endDate = endDate?.parseFlexibleToLocalDateOrNull()
)

fun TaskEntity.toDomainTask(): Task = Task(
    id = id,
    sectionId = sectionId,
    name = name,
    completed = completed,
    completedAt = completedDate,
    description = description,
    startDate = startDate?.toString(),
    endDate = endDate?.toString()
)
