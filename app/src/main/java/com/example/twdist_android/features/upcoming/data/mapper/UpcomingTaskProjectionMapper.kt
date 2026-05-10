package com.example.twdist_android.features.upcoming.data.mapper

import com.example.twdist_android.core.data.local.entity.ProjectEntity
import com.example.twdist_android.core.data.local.entity.SectionEntity
import com.example.twdist_android.core.data.local.entity.TaskEntity
import com.example.twdist_android.core.data.local.projection.TaskWithProjectProjection
import com.example.twdist_android.features.upcoming.data.dto.UpcomingTaskResponseDto
import com.example.twdist_android.features.upcoming.domain.model.UpcomingTask
import java.time.LocalDate

fun TaskWithProjectProjection.toDomainUpcomingTask(): UpcomingTask? {
    val date = startDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() } ?: return null
    return UpcomingTask(
        id = taskId,
        sectionId = sectionId,
        projectId = projectId,
        name = taskName,
        projectName = projectName,
        startDate = date
    )
}

fun UpcomingTaskResponseDto.toProjectEntity(): ProjectEntity = ProjectEntity(
    id = projectId,
    name = projectName,
    isFavorite = false,
    pendingTasks = 0
)

fun UpcomingTaskResponseDto.toSectionEntity(): SectionEntity = SectionEntity(
    id = sectionId,
    projectId = projectId,
    name = ""
)

fun UpcomingTaskResponseDto.toTaskEntity(): TaskEntity = TaskEntity(
    id = id,
    sectionId = sectionId,
    name = name,
    completed = false,
    description = description,
    startDate = startDate?.let { runCatching { LocalDate.parse(it).toString() }.getOrNull() },
    endDate = endDate
)
