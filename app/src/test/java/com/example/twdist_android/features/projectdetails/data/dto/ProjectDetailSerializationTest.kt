package com.example.twdist_android.features.projectdetails.data.dto

import com.example.twdist_android.features.explore.data.dto.ProjectSummaryDto
import com.example.twdist_android.features.projectdetails.data.dto.ProjectDetailResponseDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class ProjectDetailSerializationTest {

    @Test
    fun `project summary dto should deserialize numeric id`() {
        val input = """
            [
              {"id":1,"name":"Inbox","favorite":true,"pendingCount":4}
            ]
        """.trimIndent()

        val parsed = Json.decodeFromString<List<ProjectSummaryDto>>(input)

        assertEquals(1, parsed.size)
        assertEquals("1", parsed.first().id)
        assertEquals("Inbox", parsed.first().name)
        assertEquals(4, parsed.first().pendingCount)
    }

    @Test
    fun `project detail dto should deserialize numeric ids for project and sections`() {
        val input = """
            {
              "id": 9,
              "name": "Study",
              "favorite": false,
              "sections": [
                {"id": 10, "name": "Backlog", "taskIds": ["t-1"]}
              ]
            }
        """.trimIndent()

        val parsed = Json.decodeFromString<ProjectDetailResponseDto>(input)

        assertEquals("9", parsed.id)
        assertEquals(1, parsed.sections.size)
        assertEquals("10", parsed.sections.first().id)
    }
}
