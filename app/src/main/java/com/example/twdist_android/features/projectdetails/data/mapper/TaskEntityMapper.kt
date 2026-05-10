package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.core.data.local.entity.TaskEntity
import com.example.twdist_android.features.projectdetails.domain.model.Task

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    sectionId = sectionId,
    name = name,
    completed = completed,
    description = description,
    startDate = startDate,
    endDate = endDate
)

fun TaskEntity.toDomainTask(): Task = Task(
    id = id,
    sectionId = sectionId,
    name = name,
    completed = completed,
    description = description,
    startDate = startDate,
    endDate = endDate
)
