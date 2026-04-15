package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.features.projectdetails.data.dto.TaskResponseDto
import com.example.twdist_android.features.projectdetails.domain.model.Task
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun TaskResponseDto.toDomainTask(
    sectionId: Long,
    now: Instant = Instant.now()
): Task {
    // Intentionally mapping only fields currently used by Project Details UI.
    // Keep DTO fields (description/dates/subtasks) for forward compatibility with future task screens.
    return Task(
        id = id,
        sectionId = sectionId,
        name = name,
        completed = isCompletedByDate(now = now)
    )
}

private fun TaskResponseDto.isCompletedByDate(now: Instant = Instant.now()): Boolean {
    val parsedCompletedDate = completedDate
        ?.let { parseCompletedDate(it) }
        ?: return false
    return !parsedCompletedDate.isAfter(now)
}

private fun parseCompletedDate(value: String): Instant? {
    return runCatching { Instant.parse(value) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(value).toInstant() }.getOrNull()
        ?: runCatching { LocalDate.parse(value).atStartOfDay().toInstant(ZoneOffset.UTC) }.getOrNull()
        ?: value.toLongOrNull()?.let { millis -> runCatching { Instant.ofEpochMilli(millis) }.getOrNull() }
}
