package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.features.projectdetails.data.dto.ProjectDetailResponseDto
import com.example.twdist_android.features.projectdetails.data.dto.SectionResponseDto
import com.example.twdist_android.features.projectdetails.data.mapper.toDomainAggregate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProjectDetailsMapperTest {

    @Test
    fun `detail response should map to aggregate project and sections`() {
        val dto = ProjectDetailResponseDto(
            id = "3",
            name = "Work",
            favorite = true,
            sections = listOf(
                SectionResponseDto(id = "40", name = "Todo", taskIds = listOf("t-1"))
            )
        )

        val result = dto.toDomainAggregate()

        assertTrue(result.isSuccess)
        val aggregate = result.getOrThrow()
        assertEquals(3L, aggregate.project.id)
        assertEquals(listOf(40L), aggregate.project.sectionIds)
        assertEquals(1, aggregate.sections.size)
        assertEquals(40L, aggregate.sections.first().id)
        assertEquals(3L, aggregate.sections.first().projectId)
    }
}
