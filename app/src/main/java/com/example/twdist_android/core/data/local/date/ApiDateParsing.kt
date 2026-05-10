package com.example.twdist_android.core.data.local.date

import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Parses API/local date-ish strings into a calendar date for persistence in Room ([LocalDate]).
 */
fun String?.parseFlexibleToLocalDateOrNull(): LocalDate? {
    val raw = this?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    return runCatching { LocalDate.parse(raw) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(raw).toLocalDate() }.getOrNull()
        ?: runCatching { Instant.parse(raw).atZone(ZoneId.systemDefault()).toLocalDate() }.getOrNull()
}

/**
 * Best-effort [LocalDate] for "when this task was completed" from API strings ([TaskResponseDto.completedDate] etc.).
 */
fun String?.parseCompletedInstantToUtcLocalDateOrNull(): LocalDate? {
    val raw = this?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    val instant = runCatching { Instant.parse(raw) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(raw).toInstant() }.getOrNull()
        ?: runCatching { LocalDate.parse(raw).atStartOfDay().toInstant(ZoneOffset.UTC) }.getOrNull()
        ?: raw.toLongOrNull()?.let { ms -> runCatching { Instant.ofEpochMilli(ms) }.getOrNull() }
            ?: return null
    return instant.atZone(ZoneOffset.UTC).toLocalDate()
}
