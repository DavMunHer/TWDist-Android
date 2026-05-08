package com.example.twdist_android.features.upcoming.data.mapper

import com.example.twdist_android.features.upcoming.data.dto.UpcomingTaskResponseDto
import com.example.twdist_android.features.upcoming.domain.model.UpcomingTask
import java.time.LocalDate

fun UpcomingTaskResponseDto.toDomainUpcomingTaskOrNull(): UpcomingTask? {
    val dateString = endDate ?: startDate ?: return null
    val parsedDate = runCatching { LocalDate.parse(dateString) }.getOrNull() ?: return null

    return UpcomingTask(
        id = id,
        sectionId = sectionId,
        projectId = projectId,
        name = name,
        projectName = projectName,
        startDate = parsedDate
    )
}
