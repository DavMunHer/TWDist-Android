package com.example.twdist_android.features.explore.data.mapper

import com.example.twdist_android.features.explore.data.dto.ProjectResponseDto
import com.example.twdist_android.features.explore.data.dto.ProjectSummaryDto
import com.example.twdist_android.features.explore.data.dto.SimpleSectionDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProjectMapperTest {

    @Test
    fun `summary dto should map to ProjectSummary with pending tasks`() {
        val dto = ProjectSummaryDto(
            id = "12",
            name = "Inbox",
            favorite = true,
            pendingCount = 3
        )

        val result = dto.toDomainSummary()

        assertTrue(result.isSuccess)
        val summary = result.getOrThrow()
        assertEquals(12L, summary.id)
        assertEquals("Inbox", summary.name.asString())
        assertEquals(true, summary.isFavorite)
        assertEquals(3, summary.pendingTasks)
    }

    @Test
    fun `create response should map to Project without pending tasks and with parsed section ids`() {
        val dto = ProjectResponseDto(
            id = "9",
            name = "Study",
            favorite = false,
            sections = listOf(
                SimpleSectionDto(id = "1")
            )
        )

        val result = dto.toDomainResponse()

        assertTrue(result.isSuccess)
        val project = result.getOrThrow()
        assertEquals(9L, project.id)
        assertEquals("Study", project.name.asString())
        assertEquals(false, project.isFavorite)
        assertEquals(listOf(1L), project.sectionIds)
    }
}
