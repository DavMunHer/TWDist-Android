package com.example.twdist_android.features.today.data.mapper

import com.example.twdist_android.features.today.data.dto.TodayTaskResponseDto
import com.example.twdist_android.features.today.domain.model.TodayTask

fun TodayTaskResponseDto.toDomainTodayTask(): TodayTask = TodayTask(
    id = id,
    sectionId = sectionId,
    name = name,
    projectId = projectId,
    projectName = projectName
)
