package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.features.projectdetails.data.dto.TaskResponseDto
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TaskMapperTest {

    @Test
    fun `toDomainTask should map as not completed when completedDate is null`() {
        val dto = TaskResponseDto(
            id = 1L,
            name = "Task A",
            completedDate = null,
            completed = true
        )

        val mapped = dto.toDomainTask(sectionId = 10L)

        assertEquals(false, mapped.completed)
    }

    @Test
    fun `toDomainTask should map as completed when completedDate is in the past`() {
        val dto = TaskResponseDto(
            id = 2L,
            name = "Task B",
            completedDate = Instant.now().minus(1, ChronoUnit.DAYS).toString(),
            completed = false
        )

        val mapped = dto.toDomainTask(sectionId = 10L)

        assertEquals(true, mapped.completed)
    }

    @Test
    fun `toDomainTask should map as completed when completedDate is local date string`() {
        val dto = TaskResponseDto(
            id = 3L,
            name = "Task C",
            completedDate = LocalDate.now().minusDays(1).toString(),
            completed = false
        )

        val mapped = dto.toDomainTask(sectionId = 10L)

        assertEquals(true, mapped.completed)
    }
}
