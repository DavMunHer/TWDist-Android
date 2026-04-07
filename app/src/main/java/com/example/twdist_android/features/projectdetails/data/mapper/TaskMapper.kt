package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.features.projectdetails.data.dto.TaskResponseDto
import com.example.twdist_android.features.projectdetails.domain.model.Task

fun TaskResponseDto.toDomainTask(): Task {
    // Intentionally mapping only fields currently used by Project Details UI.
    // Keep DTO fields (description/dates/subtasks) for forward compatibility with future task screens.
    return Task(
        id = id,
        sectionId = sectionId,
        name = name,
        completed = completed
    )
}
