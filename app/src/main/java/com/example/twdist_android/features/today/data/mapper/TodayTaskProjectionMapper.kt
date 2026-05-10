package com.example.twdist_android.features.today.data.mapper

import com.example.twdist_android.core.data.local.date.parseFlexibleToLocalDateOrNull
import com.example.twdist_android.core.data.local.entity.ProjectEntity
import com.example.twdist_android.core.data.local.entity.SectionEntity
import com.example.twdist_android.core.data.local.entity.TaskEntity
import com.example.twdist_android.core.data.local.projection.TaskWithProjectProjection
import com.example.twdist_android.features.today.data.dto.TodayTaskResponseDto
import com.example.twdist_android.features.today.domain.model.TodayTask
import java.time.LocalDate

fun TaskWithProjectProjection.toDomainTodayTask(): TodayTask = TodayTask(
    id = taskId,
    sectionId = sectionId,
    name = taskName,
    projectId = projectId,
    projectName = projectName
)

fun TodayTaskResponseDto.toProjectEntity(): ProjectEntity = ProjectEntity(
    id = projectId,
    name = projectName,
    isFavorite = false
)

fun TodayTaskResponseDto.toSectionEntity(): SectionEntity = SectionEntity(
    id = sectionId,
    projectId = projectId,
    name = ""
)

fun TodayTaskResponseDto.toTaskEntity(): TaskEntity = TaskEntity(
    id = id,
    sectionId = sectionId,
    name = name,
    completed = false,
    completedDate = null,
    description = description,
    startDate = startDate.parseFlexibleToLocalDateOrNull(),
    endDate = endDate?.parseFlexibleToLocalDateOrNull()
)
