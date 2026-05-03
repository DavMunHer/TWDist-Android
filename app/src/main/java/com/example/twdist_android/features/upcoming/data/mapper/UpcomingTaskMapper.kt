package com.example.twdist_android.features.upcoming.data.mapper

import com.example.twdist_android.features.upcoming.data.dto.UpcomingTaskResponseDto
import com.example.twdist_android.features.upcoming.domain.model.UpcomingTask
import java.time.LocalDate

fun UpcomingTaskResponseDto.toDomainUpcomingTask(): UpcomingTask = UpcomingTask(
    id = id,
    sectionId = sectionId,
    projectId = projectId,
    name = name,
    projectName = projectName,
    endDate = LocalDate.parse(endDate)
)
