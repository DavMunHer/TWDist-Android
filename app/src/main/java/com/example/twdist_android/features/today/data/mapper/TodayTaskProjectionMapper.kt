package com.example.twdist_android.features.today.data.mapper

import com.example.twdist_android.core.data.local.entity.ProjectEntity
import com.example.twdist_android.core.data.local.entity.SectionEntity
import com.example.twdist_android.core.data.local.entity.TaskEntity
import com.example.twdist_android.core.data.local.projection.TaskWithProjectProjection
import com.example.twdist_android.features.today.data.dto.TodayTaskResponseDto
import com.example.twdist_android.features.today.domain.model.TodayTask
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId

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
    isFavorite = false,
    pendingTasks = 0
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
    description = description,
    startDate = parseAnyDateToLocalDateString(startDate),
    endDate = endDate
)

private fun parseAnyDateToLocalDateString(raw: String?): String? {
    if (raw.isNullOrBlank()) return null
    return runCatching { LocalDate.parse(raw).toString() }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(raw).toLocalDate().toString() }.getOrNull()
        ?: runCatching { Instant.parse(raw).atZone(ZoneId.systemDefault()).toLocalDate().toString() }.getOrNull()
}
